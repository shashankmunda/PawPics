package com.shashankmunda.pawpics.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.example.pawpics.databinding.ActivityMainBinding
import com.shashankmunda.pawpics.adapter.CatAdapter
import com.shashankmunda.pawpics.model.Cat
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.SpacesDecoration
import com.shashankmunda.pawpics.viewmodel.CatViewModel
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
        model.cats.observe(this) { response ->
            when(response){
                is Result.Success -> {
                    hideProgressBar()
                    response.data?.let{ latestCats ->
                        (cat_display.adapter as CatAdapter).updateData(latestCats as ArrayList<Cat>)
                    }
                }
                is Result.Error -> {
                    hideProgressBar()
                    response.message?.let{ errorMessage ->
                        Toast.makeText(this,"An error occurred: $errorMessage",Toast.LENGTH_SHORT).show()
                    }
                }
                is Result.Loading -> {
                    showProgressBar()
                }
            }
        }
        binding.catRefresh.setOnRefreshListener {
            onRefresh()
        }
    }

    private fun showProgressBar() {
        binding.loadingProgressBar.visibility= View.VISIBLE
    }


    private fun hideProgressBar() {
        binding.loadingProgressBar.visibility=View.INVISIBLE
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