package com.example.pawpics.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pawpics.adapter.CatAdapter
import com.example.pawpics.databinding.ActivityMainBinding
import com.example.pawpics.model.Cat
import com.example.pawpics.network.NetworkModule
import com.example.pawpics.util.SpacesDecoration
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val cat_display: RecyclerView by lazy {
        binding.catsGridViewer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
        fetchData()
    }

    private fun setupAdapter() {
        cat_display.adapter = CatAdapter(this)
        cat_display.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                gapStrategy=StaggeredGridLayoutManager.GAP_HANDLING_NONE
            }
        cat_display.addItemDecoration(SpacesDecoration(8))
    }

    private fun fetchData() {
        lifecycleScope.launch {
            val response = NetworkModule.getCatService().getImages(Random.nextInt(100))
            if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                (cat_display.adapter as CatAdapter).updateData(response.body()!! as ArrayList<Cat>)
            }
        }
    }
}