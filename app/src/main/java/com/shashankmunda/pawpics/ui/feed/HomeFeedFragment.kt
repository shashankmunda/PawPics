package com.shashankmunda.pawpics.ui.feed

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.shashankmunda.pawpics.base.BaseFragment
import com.shashankmunda.pawpics.databinding.HomeFeedFragmentBinding
import com.shashankmunda.pawpics.util.PaginationScrollListener
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.SpacesDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFeedFragment: BaseFragment<HomeFeedFragmentBinding, HomeFeedViewModel>() {
    @Inject lateinit var catAdapter: HomeFeedAdapter
    private var catsDisplay: RecyclerView?=null

    override fun getViewModelClass() = HomeFeedViewModel::class.java

    override fun getViewBinding() = HomeFeedFragmentBinding.inflate(layoutInflater)

    override var sharedViewModel = false

    var isLoading = false
    var isLastPage = false

    override fun observeData() {
        mViewModel.cats.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Result.Success -> {
                    isLoading = false
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.bottomProgressBar.visibility = View.GONE
                    response.data?.let { latestCats ->
                        (binding.catsGridViewer.adapter as HomeFeedAdapter)
                            .addItems(latestCats)
                    }
                }
                is Result.Error -> {
                    isLoading = false
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.bottomProgressBar.visibility = View.GONE
                    response.message?.let { errorMessage ->
                        Toast.makeText(
                            context,
                            "An error occurred: $errorMessage",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Result.Loading -> {
                    isLoading = true
                   if((binding.catsGridViewer.adapter as HomeFeedAdapter).isEmpty())
                       binding.loadingProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun initViews() {
        binding.catHomeToolbar.title = "PawPics"
        setupRV()
        binding.catRefresh.setOnRefreshListener {
            mViewModel.resetPageCount()
            mViewModel.fetchCatImages()
            binding.catRefresh.isRefreshing = false
        }
    }

    private fun setupRV() {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }
        binding.catsGridViewer.apply {
            layoutManager = staggeredGridLayoutManager
            setItemViewCacheSize(25)
            addItemDecoration(SpacesDecoration(8))
            adapter = catAdapter
            addOnScrollListener(object : PaginationScrollListener(staggeredGridLayoutManager){
                override fun loadMoreItems() {
                    binding.bottomProgressBar.visibility = View.VISIBLE
                    mViewModel.fetchCatImages()
                }

                override fun isLastPage() = isLastPage

                override fun isLoading() = isLoading
            })
        }
    }

    override fun onDestroyView() {
        catsDisplay = null
        super.onDestroyView()
    }
}