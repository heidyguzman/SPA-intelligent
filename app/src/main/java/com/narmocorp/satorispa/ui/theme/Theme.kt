package com.narmocorp.satorispa.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.Color

// Esquemar para el modo Claro
private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    tertiary = LightTertiary,
    background = LightBackground,
    onBackground = Color.Black, // Texto negro en fondo blanco
    surface = LightSurface,
    onSurface = Color.Black,    // Texto negro en superficie
    error = LightError,
    onError = Color.White
)

// Esquema para el modo Oscuro
private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    tertiary = DarkTertiary,
    background = DarkBackground,
    onBackground = DarkOnBackground, // Texto blanco en fondo oscuro
    surface = DarkSurface,
    onSurface = DarkOnSurface,       // Texto blanco en superficie oscura
    error = DarkError,
    onError = Color.Black
)

@Composable
fun SatoriSPATheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is not used, but kept for future reference
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Configuración de la barra de estado (Status Bar)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb() // Usa el color de fondo del tema
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asumo que tienes un archivo Typography.kt
        content = content
    )
}