package com.shashankmunda.pawpics.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pawpics.databinding.ActivityMainBinding
import com.shashankmunda.pawpics.util.Utils.Companion.clearImageCache
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
    }
    override fun onStop() {
        clearImageCache(application)
        super.onStop()
    }
}