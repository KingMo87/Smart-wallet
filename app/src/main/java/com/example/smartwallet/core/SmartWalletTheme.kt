package com.example.smartwallet.core

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF006C4F),
    secondary = Color(0xFF4E6358),
    tertiary = Color(0xFF3D6473)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF4ED8A0),
    secondary = Color(0xFF7FD4B5),
    tertiary = Color(0xFF8AC8E0)
)

@Composable
fun SmartWalletTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = MaterialTheme.typography,
        content = content
    )
}
