package com.shashankmunda.pawpics.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pawpics.R
import com.example.pawpics.databinding.HomeFragmentBinding
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.adapter.CatAdapter
import com.shashankmunda.pawpics.model.Cat
import com.shashankmunda.pawpics.util.SpacesDecoration
import com.shashankmunda.pawpics.viewmodel.CatViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.home_fragment) {
    private var _binding:HomeFragmentBinding?=null
    private val binding get()=_binding!!
    private val model:CatViewModel by viewModels()
    @Inject lateinit var catAdapter: CatAdapter
    private val catsDisplay: RecyclerView by lazy {
        binding.catsGridViewer
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        model.cats.observe(viewLifecycleOwner) { response ->
            when(response){
                is Result.Success -> {
                    hideProgressBar()
                    response.data?.let{ latestCats ->
                        (catsDisplay.adapter as CatAdapter).updateData(latestCats as ArrayList<Cat>)
                    }
                }
                is Result.Error -> {
                    hideProgressBar()
                    response.message?.let{ errorMessage ->
                        Toast.makeText(context,"An error occurred: $errorMessage",Toast.LENGTH_SHORT).show()
                    }
                }
                is Result.Loading -> {
                    showProgressBar()
                }
                else -> {}
            }
        }
        binding.catRefresh.setOnRefreshListener {
            onRefresh()
        }
    }
    private fun setupAdapter() {
        catsDisplay.adapter = catAdapter
        catsDisplay.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            }
        catsDisplay.setItemViewCacheSize(100)
        catsDisplay.addItemDecoration(SpacesDecoration(8))
    }
    private fun hideProgressBar() {
        binding.loadingProgressBar.visibility=View.INVISIBLE
    }
    private fun onRefresh() {
        model.refreshCalled()
        binding.catRefresh.isRefreshing = false
    }
    private fun showProgressBar() {
        binding.loadingProgressBar.visibility= View.VISIBLE
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}