package com.tomcz.sample.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.*

@Suppress("unused")
private val DarkColorPalette = darkColors(
    primary = DarkGrey,
    primaryVariant = Teal,
    secondary = Orange
)

private val LightColorPalette = lightColors(
    primary = DarkGrey,
    primaryVariant = Teal,
    secondary = Orange
)

@Composable
fun MainAppTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}