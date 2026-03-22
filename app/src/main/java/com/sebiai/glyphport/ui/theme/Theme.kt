package com.sebiai.glyphport.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NothingColorScheme = darkColorScheme(
    primary = NothingOffWhite,
    onPrimary = Color.Black,
    secondary = NothingYellow,
    onSecondary = Color.Black,
    tertiary = NothingRed,
    onTertiary = Color.Black,
    background = Color.Black,
    onBackground = NothingOffWhite,
    surface = Color.Black,
    onSurface = NothingOffWhite,
    outline = Color.DarkGray,
    outlineVariant = NothingYellow,
    surfaceContainer = NothingGray,
    surfaceContainerHigh = NothingElevatedGray,
    surfaceContainerHighest = NothingButtonOffGray
)

@Composable
fun GlyphPortTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NothingColorScheme,
        typography = Typography,
        content = content
    )
}