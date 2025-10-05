package com.shashankmunda.pawpics.ui

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
  primary = Color(0xFF6200EE),
  onPrimary = Color(0xFFFFFFFF),
  secondary = Color(0xFF03DAC5),
  onSecondary = Color(0xFF000000),
  tertiary = Color(0xFF018786),
)

private val DarkColors = darkColorScheme(
  primary = Color(0xFFBB86FC),
  onPrimary = Color(0xFF000000),
  secondary = Color(0xFF03DAC5),
  onSecondary = Color(0xFF000000),
  tertiary = Color(0xFF03DAC5),
)

@Composable
fun AppTheme(
  useDarkTheme: Boolean = isSystemInDarkTheme(),
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