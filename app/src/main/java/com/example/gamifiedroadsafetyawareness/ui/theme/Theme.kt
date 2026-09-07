package com.example.gamifiedroadsafetyawareness.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightAppColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = PureWhite,
    primaryContainer = NavyPrimary.copy(alpha = 0.1f),
    onPrimaryContainer = NavyPrimary,
    secondary = PoliceBlue,
    onSecondary = PureWhite,
    secondaryContainer = PoliceBlue.copy(alpha = 0.1f),
    onSecondaryContainer = PoliceBlue,
    tertiary = EmeraldGreen,
    onTertiary = PureWhite,
    tertiaryContainer = EmeraldGreen.copy(alpha = 0.1f),
    onTertiaryContainer = EmeraldGreen,
    error = TrafficRed,
    onError = PureWhite,
    errorContainer = TrafficRed.copy(alpha = 0.1f),
    onErrorContainer = TrafficRed,
    background = LightGray,
    onBackground = GraphiteInk,
    surface = PureWhite,
    onSurface = GraphiteInk,
    surfaceVariant = LightGray,
    onSurfaceVariant = SlateGray,
    outline = SlateGray.copy(alpha = 0.2f),
    inverseOnSurface = LightGray,
    inverseSurface = GraphiteInk,
    inversePrimary = InfoBlue
)

private val DarkAppColorScheme = darkColorScheme(
    // Dark mode needs a brighter accent than NavyPrimary/PoliceBlue to stay legible against a
    // near-black navy background — Royal Blue (InfoBlue) is the same police-blue family, just
    // lifted in brightness for dark-surface contrast, matching how the light scheme's very dark
    // navy would otherwise vanish against DarkBackground.
    primary = InfoBlue,
    onPrimary = PureWhite,
    primaryContainer = IndigoContainerDark,
    onPrimaryContainer = DarkOnBackground,
    secondary = PoliceBlue,
    onSecondary = PureWhite,
    secondaryContainer = CoralContainerDark,
    onSecondaryContainer = DarkOnBackground,
    tertiary = EmeraldGreen,
    onTertiary = DarkBackground,
    tertiaryContainer = EmeraldContainerDark,
    onTertiaryContainer = DarkOnBackground,
    error = TrafficRed,
    onError = PureWhite,
    errorContainer = TrafficRedContainerDark,
    onErrorContainer = DarkOnBackground,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnBackground,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    inverseOnSurface = DarkBackground,
    inverseSurface = DarkOnBackground,
    inversePrimary = NavyPrimary
)

@Composable
fun GamifiedRoadSafetyAwarenessTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkAppColorScheme else LightAppColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = Color.Transparent.toArgb()
            @Suppress("DEPRECATION")
            window.navigationBarColor = Color.Transparent.toArgb()

            WindowCompat.setDecorFitsSystemWindows(window, false)

            // Dark icons on light system bars in light mode; light icons on dark system bars in dark mode.
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}