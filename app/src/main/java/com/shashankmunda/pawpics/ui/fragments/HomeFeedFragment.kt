package com.shashankmunda.pawpics.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pawpics.R
import com.example.pawpics.databinding.HomeFeedFragmentBinding
import com.shashankmunda.pawpics.base.BaseFragment
import com.shashankmunda.pawpics.model.Cat
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.SpacesDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFeedFragment: BaseFragment<HomeFeedFragmentBinding, HomeViewModel>() {
    @Inject lateinit var catAdapter: HomeFeedAdapter
    private var catsDisplay: RecyclerView?=null
    override fun getViewModelClass() = HomeViewModel::class.java

    override fun getViewBinding() = HomeFeedFragmentBinding.inflate(layoutInflater)

    override var sharedViewModel = true

    override fun observeData() {
        mViewModel.cats.observe(viewLifecycleOwner) { response ->
            updateUI(response,binding)
        }
    }

    override fun initViews() {
        binding.catHomeToolbar.title = "PawPics"
        setupAdapter(binding)
        binding.catRefresh.setOnRefreshListener {
            onRefresh(binding)
        }
    }

    private fun updateUI(response: Result<List<Cat>>,binding: HomeFeedFragmentBinding) {
        when (response) {
            is Result.Success -> {
                binding.loadingProgressBar.visibility = View.GONE
                response.data?.let { latestCats ->
                    (catsDisplay!!.adapter as HomeFeedAdapter)
                        .updateData(latestCats as ArrayList<Cat>)
                }
            }
            is Result.Error -> {
                binding.loadingProgressBar.visibility = View.GONE
                response.message?.let { errorMessage ->
                    Toast.makeText(
                        context,
                        "An error occurred: $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            is Result.Loading -> {
                binding.loadingProgressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun setupAdapter(binding: HomeFeedFragmentBinding) {
        catsDisplay=binding.catsGridViewer
        catsDisplay?.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            }
        catsDisplay?.setItemViewCacheSize(25)
        catsDisplay?.addItemDecoration(SpacesDecoration(8))
        catsDisplay?.adapter = catAdapter
    }

    private fun onRefresh(binding: HomeFeedFragmentBinding) {
        mViewModel.refreshCalled()
        binding.catRefresh.isRefreshing = false
    }

    override fun onDestroyView() {
        catsDisplay=null
        super.onDestroyView()
    }
}