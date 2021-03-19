package com.alorma.drawer_ui_modules.design

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOff
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alorma.drawer_base.DebugDrawerModule
import com.alorma.drawer_modules.actions.SwitchAction

@Composable
fun DesignModule(
    modifier: Modifier = Modifier,
    config: DebugGridStateConfig,
    onChange: (DebugGridStateConfig) -> Unit,
) {
    DebugDrawerModule(
        modifier = modifier,
        icon = {
            val icon = if (config.isEnabled) {
                Icons.Default.GridOn
            } else {
                Icons.Default.GridOff
            }
            Icon(
                imageVector = icon,
                contentDescription = "Design icon",
            )
        },
        title = "Design"
    ) {
        val text = if (config.isEnabled) {
            "Grid enabled"
        } else {
            "Grid disabled"
        }
        SwitchAction(text = text, isChecked = config.isEnabled) { enabled ->
            val result = config.copy(isEnabled =  enabled)
            onChange(result)
        }
    }
}