package net.kelmer.correostracker.list.compose

import androidx.compose.ui.graphics.vector.ImageVector

data class ActionItem(
    val name: String,
    val icon: ImageVector? = null,
    val action: () -> Unit,
    val order: Int
)
