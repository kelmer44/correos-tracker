package net.kelmer.correostracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import timber.log.Timber

private val LightColors = lightColorScheme(
    primary = primaryLight,
    surface = surfaceLight,
    secondary = secondaryLight,
    background = backgroundLight,
    primaryContainer = correosBlue
)


private val DarkColors = darkColorScheme(
    primary = primaryDark,
//    onPrimary = onPrimaryDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    secondary = secondaryDark,
    background = backgroundDark,
)

@Composable
fun CorreosTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    overrideTypography: Typography? = null,
    overrideColors: ColorScheme? = null,
    content: @Composable () -> Unit,
) {
    Timber.i("Recreating Theme")
    val colors = overrideColors ?: if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }
    val typography = overrideTypography ?: MaterialTheme.typography
    MaterialTheme(
        colorScheme = colors,
        content = content,
        typography = typography
    )
}
