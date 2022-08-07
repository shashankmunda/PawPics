package com.shashankmunda.pawpics.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pawpics.R
import com.shashankmunda.pawpics.util.Utils.Companion.clearImageCache
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onStop() {
        clearImageCache(application)
        super.onStop()
    }
}