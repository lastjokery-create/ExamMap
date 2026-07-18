package jp.maple.exammap.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = OrangeMain,
            onPrimary = ColorWhite,
    background = ColorGrayBg,
    surface = ColorWhite,
    onBackground = ColorDark,
    onSurface = ColorDark,
    outline = ColorBorder,
)

@Composable
fun ExamMapTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}