package com.shashankmunda.pawpics.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
  primary = Color(0xFF6200EE),
  onPrimary = Color(0xFFFFFFFF),
  secondary = Color(0xFF03DAC6),
  onSecondary = Color(0xFF000000),
  tertiary = Color(0xFF018786),
  surface = Color(0xFFF5F5F5)
)

private val DarkColors = darkColorScheme(
  primary = Color(0xFF6200EE),
  onPrimary = Color(0xFFFFFFFF),
  secondary = Color(0xFF121212),
  onSecondary = Color(0xFFB3B3B3),
  tertiary = Color(0xFF03DAC5),
  surface= Color(0xFF1E1E1E)
)

@Composable
fun AppTheme(
  useDarkTheme: Boolean = isDarkMode(),
  content: @Composable() () -> Unit
){
  val colors = if (!useDarkTheme) {
    LightColors
  } else {
    DarkColors
  }
  MaterialTheme(
    colorScheme = colors,
    content = content
  )
}

@Composable
fun isDarkMode() = when(AppCompatDelegate.getDefaultNightMode()) {
  AppCompatDelegate.MODE_NIGHT_YES -> true
  AppCompatDelegate.MODE_NIGHT_NO -> false
  else -> isSystemInDarkTheme()
}