package com.example.pawpics.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.example.pawpics.adapter.CatAdapter
import com.example.pawpics.databinding.ActivityMainBinding
import com.example.pawpics.model.Cat
import com.example.pawpics.util.SpacesDecoration
import com.example.pawpics.viewmodel.CatViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val model: CatViewModel by viewModels()
    @Inject lateinit var catAdapter: CatAdapter
    private val cat_display: RecyclerView by lazy {
        binding.catsGridViewer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
        model.cats.observe(this) { cats ->
            (cat_display.adapter as CatAdapter).updateData(cats as ArrayList<Cat>)
        }
        binding.catRefresh.setOnRefreshListener {
            onRefresh()
        }
    }
    private fun onRefresh() {
        model.refreshCalled()
        binding.catRefresh.isRefreshing = false
    }

    private fun setupAdapter() {
        cat_display.adapter = catAdapter
        cat_display.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            }
        cat_display.setItemViewCacheSize(100)
        cat_display.addItemDecoration(SpacesDecoration(8))
    }

    override fun onStart() {
        super.onStart()
        clearImageCache()
    }

    @OptIn(ExperimentalCoilApi::class)
    private fun clearImageCache(){
        imageLoader.diskCache?.clear()
    }
}