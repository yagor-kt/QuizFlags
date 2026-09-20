package com.example.quizflags.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Светлая цветовая схема Material 3.
private val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = BlueLight,
    onPrimaryContainer = BlueDark,
    secondary = Green,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = GreenDark,
    error = Red,
    onError = androidx.compose.ui.graphics.Color.White,
    surface = GraySurface,
    onSurface = GrayOnSurface,
)

// Единая тема приложения.
@Composable
fun QuizFlagsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content,
    )
}