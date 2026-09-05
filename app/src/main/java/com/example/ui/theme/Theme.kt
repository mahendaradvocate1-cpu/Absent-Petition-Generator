package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = PolishPurpleLight,
    onPrimary = PolishPurpleDark,
    primaryContainer = PolishPurpleDark,
    onPrimaryContainer = PolishPurpleContainer,
    secondary = PolishPurpleLight,
    onSecondary = PolishPurpleDark,
    tertiary = LegalGoldLight,
    background = Color(0xFF141218),
    surface = Color(0xFF211F26),
    surfaceVariant = Color(0xFF49454F),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
    outline = Color(0xFF938F99),
  )

private val LightColorScheme =
  lightColorScheme(
    primary = PolishPurplePrimary,
    onPrimary = Color.White,
    primaryContainer = PolishPurpleContainer,
    onPrimaryContainer = PolishPurpleDark,
    secondary = PolishPurplePrimary,
    onSecondary = Color.White,
    secondaryContainer = PolishSurfaceVariant,
    onSecondaryContainer = PolishPurpleDark,
    tertiary = LegalGoldLight,
    background = PolishBackground,
    surface = Color.White,
    surfaceVariant = PolishSurfaceVariant,
    onBackground = PolishTextPrimary,
    onSurface = PolishTextPrimary,
    onSurfaceVariant = PolishTextSecondary,
    outline = PolishBorder,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disable dynamic color so the requested "Professional Polish" styling is faithfully displayed
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
