package com.runtracker.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = ClaudeOrange,
    onPrimary = Color.White,
    primaryContainer = ClaudeOrangeLight,
    onPrimaryContainer = ClaudeOrangeDark,
    secondary = ClaudeGreen,
    onSecondary = Color.White,
    secondaryContainer = ClaudeGreenLight,
    onSecondaryContainer = ClaudeGreen,
    tertiary = ClaudeBlue,
    onTertiary = Color.White,
    tertiaryContainer = ClaudeBlueLight,
    error = ClaudeRed,
    onError = Color.White,
    errorContainer = ClaudeRedLight,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondary,
    outline = BorderColor,
    outlineVariant = DividerColor
)

@Composable
fun RunTrackerTheme(content: @Composable () -> Unit) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.White.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
