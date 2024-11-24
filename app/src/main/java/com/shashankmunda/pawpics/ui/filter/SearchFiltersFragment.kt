package com.shashankmunda.pawpics.ui.filter

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.shashankmunda.pawpics.R
import com.shashankmunda.pawpics.base.BaseFragment
import com.shashankmunda.pawpics.data.Breed
import com.shashankmunda.pawpics.databinding.SearchFilterFragmentBinding
import com.shashankmunda.pawpics.ui.feed.HomeFeedViewModel
import com.shashankmunda.pawpics.util.Result.Error
import com.shashankmunda.pawpics.util.Result.Loading
import com.shashankmunda.pawpics.util.Result.Success
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFiltersFragment : BaseFragment<SearchFilterFragmentBinding, HomeFeedViewModel>() {
  override fun getViewModelClass() = HomeFeedViewModel::class.java

  override fun getViewBinding() = SearchFilterFragmentBinding.inflate(layoutInflater)

  override var sharedViewModel = true

  private var currSelectedFilters = mutableListOf<Breed>()

  override fun observeData() {
    mViewModel.selectedFilters.observe(viewLifecycleOwner){response ->
      when(response){
        is Success -> currSelectedFilters = response.data as MutableList<Breed>
        is Error -> TODO()
        is Loading -> TODO()
      }
    }
    mViewModel.filters.observe(viewLifecycleOwner) { response ->
        when(response){
            is Success -> {
                val parentLayout = LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                }
              val inflater = LayoutInflater.from(context)
                    for ( i in response.data!!.indices step 10) {
                      val rowLayout = LinearLayout(context).apply {
                        orientation = LinearLayout.HORIZONTAL
                      }
                      for (j in i until (i + 10).coerceAtMost(response.data.size)) {
                        val filterChip = inflater.inflate(R.layout.chip_filter_tag, rowLayout, false) as Chip
                        filterChip.apply {
                          text = response.data[j].name
                          isChecked = mViewModel.selectedFilters.value?.data?.contains(response.data[j]) == true
                          setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                              currSelectedFilters.add(mViewModel.filters.value?.data?.filter { it.name == text }!![0])
                            } else {
                              currSelectedFilters.remove(mViewModel.filters.value?.data?.filter { it.name == text }!![0])
                            }
                          }
                        }
                        rowLayout.addView(filterChip)
                      }
                      parentLayout.addView(rowLayout)
                    }
                binding.filterScrollView.addView(parentLayout);
                binding.loadingProgressBar.visibility = View.GONE
                //filterTagAdapter.setItems(response.data!!)
            }
            is Error -> {
            }

            is Loading -> {
              binding.loadingProgressBar.visibility = View.VISIBLE
            }
        }
    }
  }

  override fun initViews() {
    setupToolbar()
  }

  private fun setupToolbar(){
    binding.catImageToolbar.apply {
      inflateMenu(R.menu.search_filter_menu)
      setOnMenuItemClickListener(searchMenuListener)
      setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
      setNavigationOnClickListener {
        findNavController().popBackStack()
      }
    }
  }

  private val searchMenuListener = Toolbar.OnMenuItemClickListener { item ->
    when(item.itemId){
      R.id.confirm ->{
        mViewModel.setFiltersForSelected(currSelectedFilters)
        findNavController().popBackStack()
        true
      }
      else -> false
    }
  }
}