package jp.maple.exammap.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = WhitePrimary,

    primaryContainer = OrangeDark,
    onPrimaryContainer = WhitePrimary,

    secondary = BlackPrimary,
    onSecondary = WhitePrimary,

    background = Background,
    onBackground = TextPrimary,

    surface = Surface,
    onSurface = TextPrimary,

    outline = Divider
)

private val DarkColorScheme = darkColorScheme(
    primary = OrangePrimary,
    onPrimary = BlackPrimary,

    primaryContainer = OrangeDark,
    onPrimaryContainer = WhitePrimary,

    secondary = WhitePrimary,
    onSecondary = BlackPrimary,

    background = BlackPrimary,
    onBackground = WhitePrimary,

    surface = ColorDarkSurface,
    onSurface = WhitePrimary,

    outline = TextSecondary
)

@Composable
fun ExamMapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (darkTheme) DarkColorScheme
        else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}