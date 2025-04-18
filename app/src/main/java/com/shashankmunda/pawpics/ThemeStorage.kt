package com.shashankmunda.pawpics

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface IThemeStorage {
  fun isDarkModeApplied(): Boolean?
  fun setDarkModeApplied(value: Boolean)
}

@Singleton
class ThemeStorage @Inject constructor(@ApplicationContext var context: Context): IThemeStorage {
  private val prefs by lazy {
    context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
  }

  override fun isDarkModeApplied(): Boolean? {
    if (!prefs.contains(KEY_DARK_MODE_APPLIED)) return null
    return prefs.getBoolean(KEY_DARK_MODE_APPLIED, false)
  }

  override fun setDarkModeApplied(value: Boolean) {
    prefs.edit { putBoolean(KEY_DARK_MODE_APPLIED, value) }
  }

  private companion object{
    const val KEY_DARK_MODE_APPLIED = "KEY_DARK_MODE_APPLIED"
  }
}