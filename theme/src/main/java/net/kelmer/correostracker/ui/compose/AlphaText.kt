package net.kelmer.correostracker.ui.compose

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun AlphaText(
    alpha: Float,
    color: Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)) {
        content()
    }
}
