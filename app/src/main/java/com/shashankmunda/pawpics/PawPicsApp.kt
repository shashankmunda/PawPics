package com.shashankmunda.pawpics

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PawPicsApp:Application(){
    override fun onTerminate() {
        applicationContext.externalCacheDir!!.deleteRecursively()
        super.onTerminate()
    }
}