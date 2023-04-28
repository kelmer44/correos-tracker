package net.kelmer.correostracker.ui.compose

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class ActionItem(
    val name: String,
    val icon: ImageVector? = null,
    val painterIcon: Painter? = null,
    val action: () -> Unit = {},
    val enabled: Boolean = true
)
