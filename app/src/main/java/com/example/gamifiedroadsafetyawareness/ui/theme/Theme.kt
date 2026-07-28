package com.example.gamifiedroadsafetyawareness.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = TextInverse,
    primaryContainer = VividViolet,
    onPrimaryContainer = TextPrimaryWhite,
    secondary = VividViolet,
    onSecondary = TextPrimaryWhite,
    secondaryContainer = DarkGlassSurfaceVariant,
    onSecondaryContainer = ElectricCyan,
    tertiary = NeonEmerald,
    onTertiary = TextInverse,
    tertiaryContainer = DarkGlassSurfaceVariant,
    onTertiaryContainer = NeonEmerald,
    error = DangerCoral,
    onError = TextInverse,
    errorContainer = DarkGlassSurfaceVariant,
    onErrorContainer = DangerCoral,
    background = DeepNavyBlack,
    onBackground = TextPrimaryWhite,
    surface = DarkGlassSurface,
    onSurface = TextPrimaryWhite,
    surfaceVariant = DarkGlassSurfaceVariant,
    onSurfaceVariant = TextSecondaryMuted,
    outline = OutlineDark,
    inverseOnSurface = DeepNavyBlack,
    inverseSurface = TextPrimaryWhite,
    inversePrimary = ElectricCyan
)

@Composable
fun GamifiedRoadSafetyAwarenessTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            
            // Draw content behind system bars
            WindowCompat.setDecorFitsSystemWindows(window, false)
            
            // Set dark theme appearance for status and navigation bars
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}