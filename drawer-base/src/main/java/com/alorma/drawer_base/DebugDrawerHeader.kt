package com.alorma.drawer_base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerModuleHeader(
    icon: @Composable (() -> Unit)? = null,
    title: String,
    expandedState: Boolean = false,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .semantics {
                testTag = "Module header $title"
            },
        color = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.secondary,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                DrawerModuleHeaderIcon(icon)
            } else {
                Spacer(modifier = Modifier.requiredSize(36.dp))
            }
            DrawerModuleHeaderText(
                modifier = Modifier.weight(1f),
                title = title
            )
            DrawerModuleHeaderIcon {
                if (expandedState) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropUp,
                        contentDescription = "Arrow",
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Arrow",
                    )
                }
            }
        }
    }
}

@Composable
internal fun DrawerModuleHeaderIcon(
    content: @Composable () -> Unit = {},
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colors.onSurface,
    ) {
        Box(
            modifier = Modifier.requiredSize(36.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
internal fun DrawerModuleHeaderText(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        color = MaterialTheme.colors.primary,
        modifier = modifier,
        text = title,
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Bold,
    )
}