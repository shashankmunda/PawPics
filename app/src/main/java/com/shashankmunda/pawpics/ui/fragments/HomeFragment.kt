package com.shashankmunda.pawpics.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pawpics.R
import com.example.pawpics.databinding.HomeFragmentBinding
import com.shashankmunda.pawpics.adapter.CatAdapter
import com.shashankmunda.pawpics.model.Cat
import com.shashankmunda.pawpics.ui.CatViewModel
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.SpacesDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.home_fragment) {
    private val catViewModel: CatViewModel by viewModels()
    @Inject lateinit var catAdapter: CatAdapter
    private lateinit var catsDisplay: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding=HomeFragmentBinding.bind(view)
        binding.catHomeToolbar.title = "PawPics"
        //(requireActivity() as AppCompatActivity).setSupportActionBar(binding.catHomeToolbar)
        setupAdapter(binding)
        catViewModel.cats.observe(viewLifecycleOwner) { response ->
            updateUI(response,binding)
        }
        binding.catRefresh.setOnRefreshListener {
            onRefresh(binding)
        }
    }

    private fun updateUI(response: Result<List<Cat>>,binding: HomeFragmentBinding) {
        when (response) {
            is Result.Success -> {
                hideProgressBar(binding)
                Log.d("HELLO","hello")
                response.data?.let { latestCats ->
                    (catsDisplay.adapter as CatAdapter).updateData(latestCats as ArrayList<Cat>)
                }
            }
            is Result.Error -> {
                hideProgressBar(binding)
                response.message?.let { errorMessage ->
                    Toast.makeText(
                        context,
                        "An error occurred: $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            is Result.Loading -> {
                showProgressBar(binding)
            }
        }
    }

    private fun setupAdapter(binding: HomeFragmentBinding) {
        catsDisplay=binding.catsGridViewer
        catsDisplay.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            }
        catsDisplay.setItemViewCacheSize(100)
        catsDisplay.addItemDecoration(SpacesDecoration(8))
        catsDisplay.adapter = catAdapter
    }
    private fun hideProgressBar(binding: HomeFragmentBinding) {
        binding.loadingProgressBar.visibility=View.INVISIBLE
    }
    private fun onRefresh(binding: HomeFragmentBinding) {
        catViewModel.refreshCalled()
        binding.catRefresh.isRefreshing = false
    }
    private fun showProgressBar(binding: HomeFragmentBinding) {
        binding.loadingProgressBar.visibility= View.VISIBLE
    }
}