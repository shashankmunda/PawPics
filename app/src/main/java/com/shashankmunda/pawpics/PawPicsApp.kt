package com.shashankmunda.pawpics

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.shashankmunda.pawpics.util.Utils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PawPicsApp:Application(),DefaultLifecycleObserver{
}