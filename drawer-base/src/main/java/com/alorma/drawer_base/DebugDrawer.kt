package com.alorma.drawer_base

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


/**
 * Possible values of [DebugDrawerState].
 */
enum class DebugDrawerValue {
    /**
     * The state of the drawer when it is closed.
     */
    Closed,

    /**
     * The state of the drawer when it is open.
     */
    Open
}

/**
 * State of the [_root_ide_package_.com.alorma.drawer_base.DebugDrawerLayout() {

}] composable.
 *
 * @param initialValue The initial value of the state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 */
@Suppress("NotCloseable")
@OptIn(ExperimentalMaterialApi::class)
@Stable
class DebugDrawerState(
    initialValue: DebugDrawerValue,
    confirmStateChange: (DebugDrawerValue) -> Boolean = { true },
) : SwipeableState<DebugDrawerValue>(
    initialValue = initialValue,
    animationSpec = DebugDrawerDefaults.AnimationSpec,
    confirmStateChange = confirmStateChange
) {
    /**
     * Whether the drawer is open.
     */
    val isOpen: Boolean
        get() = currentValue == DebugDrawerValue.Open

    /**
     * Whether the drawer is closed.
     */
    val isClosed
        get() = currentValue == DebugDrawerValue.Closed

    /**
     * Open the drawer with animation and suspend until it if fully opened or animation has been
     * cancelled. This method will throw [CancellationException] if the animation is
     * interrupted
     *
     * @return the reason the open animation ended
     */
    suspend fun open() = animateTo(DebugDrawerValue.Open)

    /**
     * Change the drawer with animation and suspend until it if fully (opened | closed) or
     * animation has been cancelled. This method will throw [CancellationException]
     * if the animation is interrupted
     *
     * @return the reason the open animation ended
     */
    suspend fun toggle() {
        return if (isOpen) {
            animateTo(DebugDrawerValue.Closed)
        } else {
            animateTo(DebugDrawerValue.Open)
        }
    }

    /**
     * Close the drawer with animation and suspend until it if fully closed or animation has been
     * cancelled. This method will throw [CancellationException] if the animation is
     * interrupted
     *
     * @return the reason the close animation ended
     */
    suspend fun close() = animateTo(DebugDrawerValue.Closed)

    companion object {
        /**
         * The default [Saver] implementation for [DrawerState].
         */
        fun Saver(confirmStateChange: (DebugDrawerValue) -> Boolean) =
            Saver<DebugDrawerState, DebugDrawerValue>(
                save = { it.currentValue },
                restore = { DebugDrawerState(it, confirmStateChange) }
            )
    }
}

/**
 * Create and [remember] a [DebugDrawerState] with the default animation clock.
 *
 * @param initialValueDebug The initial value of the state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 */
@Composable
fun rememberDebugDrawerState(
    initialValueDebug: DebugDrawerValue,
    confirmStateChange: (DebugDrawerValue) -> Boolean = { true },
): DebugDrawerState {
    return rememberSaveable(saver = DebugDrawerState.Saver(confirmStateChange)) {
        DebugDrawerState(initialValueDebug, confirmStateChange)
    }
}

/**
 *
 * @param modifier optional modifier for the drawer
 * @param drawerShape shape of the drawer sheet
 * @param drawerElevation drawer sheet elevation. This controls the size of the shadow below the
 * drawer sheet
 * @param bodyContent content of the rest of the UI
 *
 * @throws IllegalStateException when parent has [Float.POSITIVE_INFINITY] width
 */
@Composable
@OptIn(ExperimentalMaterialApi::class)
fun DebugDrawerLayout(
    modifier: Modifier = Modifier,
    enableShake: Boolean = true,
    drawerColors: Colors = DebugDrawerDefaults.Colors,
    drawerShape: Shape = MaterialTheme.shapes.large,
    drawerElevation: Dp = DebugDrawerDefaults.Elevation,
    drawerContentModifier: Modifier = Modifier,
    drawerModules: @Composable ColumnScope.(DebugDrawerState) -> Unit = { },
    bodyContent: @Composable (DebugDrawerState) -> Unit,
) {

    val debugDrawerState: DebugDrawerState = rememberDebugDrawerState(DebugDrawerValue.Closed)
    val scope = rememberCoroutineScope()

    if (enableShake) {
        enableShake(scope, debugDrawerState)
    }

    BoxWithConstraints(modifier.fillMaxSize()) {
        if (!constraints.hasBoundedWidth) {
            throw IllegalStateException("Drawer shouldn't have infinite width")
        }

        val minValue = constraints.maxWidth.toFloat()
        val maxValue = 0f

        val anchors = mapOf(minValue to DebugDrawerValue.Closed, maxValue to DebugDrawerValue.Open)
        val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

        Box(
            Modifier.swipeable(
                state = debugDrawerState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal,
                enabled = true,
                reverseDirection = isRtl,
                velocityThreshold = DebugDrawerDefaults.VelocityThreshold,
                resistance = null
            )
        ) {
            Box {
                bodyContent(debugDrawerState)
            }
            MaterialTheme(
                colors = drawerColors,
                shapes = MaterialTheme.shapes,
                typography = MaterialTheme.typography
            ) {
                Scrim(
                    open = debugDrawerState.isOpen,
                    onClose = { scope.launch { debugDrawerState.close() } },
                    fraction = {
                        calculateFraction(
                            minValue,
                            maxValue,
                            debugDrawerState.offset.value
                        )
                    },
                    color = DebugDrawerDefaults.scrimColor
                )
                Surface(
                    color = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.onSurface,
                    modifier = with(LocalDensity.current) {
                        Modifier
                            .width(this@BoxWithConstraints.constraints.maxWidth.toDp())
                            .height(this@BoxWithConstraints.constraints.maxHeight.toDp())
                            .padding(start = DebugDrawerDefaults.StartPadding)
                    }
                        .semantics {
                            if (debugDrawerState.isOpen) {
                                dismiss(action = { scope.launch { debugDrawerState.close() }; true })
                            }
                        }
                        .offset { IntOffset(debugDrawerState.offset.value.roundToInt(), 0) },
                    shape = drawerShape,
                    elevation = drawerElevation
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .then(drawerContentModifier)
                    ) {
                        drawerModules(debugDrawerState)
                    }
                }
            }
        }
    }
}

@Composable
private fun enableShake(
    scope: CoroutineScope,
    debugDrawerState: DebugDrawerState,
) {
    val sensorManager =
        LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val shakeDetector = ShakeDetector()
    shakeDetector.listener = { scope.launch { debugDrawerState.toggle() } }

    DisposableEffect(key1 = "sensor") {
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
        onDispose {
            sensorManager.unregisterListener(shakeDetector)
        }
    }
}

private fun calculateFraction(a: Float, b: Float, pos: Float) =
    ((pos - a) / (b - a)).coerceIn(0f, 1f)

@Composable
private fun Scrim(
    open: Boolean,
    onClose: () -> Unit,
    fraction: () -> Float,
    color: Color,
) {
    val dismissDrawer = if (open) {
        Modifier.pointerInput(Unit) { detectTapGestures(onTap = { onClose() }) }
    } else {
        Modifier
    }

    Canvas(
        Modifier
            .fillMaxSize()
            .then(dismissDrawer)
    ) {
        drawRect(color, alpha = fraction())
    }
}

object DebugDrawerDefaults {
    val Colors: Colors = darkColors(
        primary = Color(0xFF86D7F5),
        primaryVariant = Color(0xFF1D668F),
        onPrimary = Color(0xFF1D668F),

        secondary = Color(0xFFFF6340),
        onSecondary = Color.White,

        background = Color(0xFF000000),
        surface = Color(0xFF2D3133),
    )

    val StartPadding = 56.dp
    val VelocityThreshold = 400.dp

    val AnimationSpec = TweenSpec<Float>(durationMillis = 100)

    /**
     * Default Elevation for drawer sheet as specified in material specs
     */
    val Elevation = 16.dp

    val scrimColor: Color
        @Composable
        get() = MaterialTheme.colors.surface.copy(alpha = ScrimOpacity)

    /**
     * Default alpha for scrim color
     */
    const val ScrimOpacity = 0.32f
}