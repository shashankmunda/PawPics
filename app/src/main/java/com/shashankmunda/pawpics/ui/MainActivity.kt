package com.shashankmunda.pawpics.ui

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pawpics.R
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
    fun setupActionBar(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.cat_host_fragment)
        val navController= navHostFragment?.findNavController()
        setupActionBarWithNavController(navController!!)
    }
}