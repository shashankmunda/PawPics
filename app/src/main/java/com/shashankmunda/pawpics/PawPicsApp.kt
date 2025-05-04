package com.shashankmunda.pawpics

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PawPicsApp:Application(),DefaultLifecycleObserver{

  @Inject lateinit var themesStorage: ThemeStorage

  override fun onCreate() {
    super<Application>.onCreate()
    setDayNightMode()
  }

  private fun setDayNightMode() {
    val themeMode = when (themesStorage.isDarkModeApplied()) {
      null -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
      true -> AppCompatDelegate.MODE_NIGHT_YES
      false -> AppCompatDelegate.MODE_NIGHT_NO
    }
    AppCompatDelegate.setDefaultNightMode(themeMode)
  }
}