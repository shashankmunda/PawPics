package com.shashankmunda.pawpics

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PawPicsApp:Application(),DefaultLifecycleObserver{
}