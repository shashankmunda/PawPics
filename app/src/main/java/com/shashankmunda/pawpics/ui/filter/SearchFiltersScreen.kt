package com.shashankmunda.pawpics.ui.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.shashankmunda.pawpics.data.Breed
import com.shashankmunda.pawpics.ui.feed.HomeFeedViewModel
import com.shashankmunda.pawpics.util.Result
import kotlinx.coroutines.launch

@Composable
fun SearchFiltersScreen(viewModel: HomeFeedViewModel = hiltViewModel(), onBackPressed: () -> Unit) {
  var currSelectedFilters = remember { mutableListOf<Breed>() }
  val filters by viewModel.filters.observeAsState()
  val selectedFilters by viewModel.selectedFilters.observeAsState()
  val scope = rememberCoroutineScope()
  LaunchedEffect(selectedFilters) {
    if (selectedFilters is Result.Success) {
      currSelectedFilters = if(currSelectedFilters.isNotEmpty())
        (selectedFilters as Result.Success).data as MutableList<Breed>
      else mutableListOf<Breed>()
    }
  }
  SearchFiltersContent(
    filters,
    selectedFilters,
    { isChecked, text ->
      scope.launch {
        if (isChecked) {
          currSelectedFilters.add(viewModel.filters.value?.data?.filter { it.name == text }!![0])
        } else {
          currSelectedFilters.remove(viewModel.filters.value?.data?.filter { it.name == text }!![0])
        }
      }
    }, onBackPressed,
    {
      scope.launch {
        viewModel.setFiltersForSelected(currSelectedFilters);
        viewModel.resetPageCount()
        viewModel.fetchCatImages()
      }
      onBackPressed()
    })
}