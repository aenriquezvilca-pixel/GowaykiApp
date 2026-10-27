package com.tecsup.gowaykiapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GwOrange,
    onPrimary = Color.White,
    primaryContainer = GwOrangeDeep,
    onPrimaryContainer = Color(0xFFFFD9C2),
    background = GwDarkBg,
    onBackground = GwDarkText,
    surface = GwDarkSurface,
    onSurface = GwDarkText,
    surfaceVariant = Color(0xFF2A2420),
    onSurfaceVariant = GwDarkMuted,
    outline = GwDarkBorder,
    error = GwRed
)

private val LightColorScheme = lightColorScheme(
    primary = GwOrange,
    onPrimary = Color.White,
    primaryContainer = GwOrangeSoft,
    onPrimaryContainer = Color(0xFF7A3510),
    background = GwLightBg,
    onBackground = GwLightText,
    surface = GwLightSurface,
    onSurface = GwLightText,
    surfaceVariant = Color(0xFFF4EDE6),
    onSurfaceVariant = GwLightMuted,
    outline = GwLightBorder,
    error = GwRed
)

@Composable
fun GowaykiAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
