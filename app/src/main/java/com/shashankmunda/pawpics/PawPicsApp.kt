package com.shashankmunda.pawpics

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.shashankmunda.pawpics.util.ReferrerManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PawPicsApp : Application(), DefaultLifecycleObserver, SingletonImageLoader.Factory {

    @Inject
    lateinit var themesStorage: ThemeStorage

    @Inject
    lateinit var referrerManager: ReferrerManager

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate() {
        super<Application>.onCreate()
        setDayNightMode()
        referrerManager.fetchReferrerIfNeeded()
    }

    override fun newImageLoader(context: Context): ImageLoader {
        return imageLoader
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