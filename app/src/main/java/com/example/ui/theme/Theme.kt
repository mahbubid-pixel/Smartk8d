package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueLight.copy(alpha = 0.35f),
    onPrimaryContainer = PrimaryBlueDark,
    secondary = CoralOrange,
    onSecondary = Color.White,
    secondaryContainer = CoralOrangeLight,
    onSecondaryContainer = Color(0xFF9A3412),
    tertiary = MintGreen,
    onTertiary = Color.White,
    tertiaryContainer = MintGreenLight,
    onTertiaryContainer = Color(0xFF065F46),
    background = KidBackground,
    onBackground = KidTextPrimary,
    surface = KidSurface,
    onSurface = KidTextPrimary,
    surfaceVariant = KidSurfaceVariant,
    onSurfaceVariant = KidTextSecondary,
    outline = KidBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueLight,
    onPrimary = PrimaryBlueDark,
    secondary = CoralOrange,
    onSecondary = Color.White,
    tertiary = MintGreen,
    onTertiary = Color.White,
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
)

@Composable
fun SmartKidsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent bright cheerful brand colors for children
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
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
