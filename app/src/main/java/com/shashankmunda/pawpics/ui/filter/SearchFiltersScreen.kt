package com.shashankmunda.pawpics.ui.filter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.shashankmunda.pawpics.data.Breed
import com.shashankmunda.pawpics.ui.AppTheme
import com.shashankmunda.pawpics.ui.feed.HomeFeedViewModel
import com.shashankmunda.pawpics.util.Result
import kotlinx.coroutines.launch

@Composable
fun SearchFiltersContent(
  filters: Result<List<Breed>>?,
  selectedFilters: Result<List<Breed>>?,
  onClick: (Boolean, String) -> Unit,
  onBackPressed: () -> Unit,
  onSaveFilter: () -> Unit
) {

  if (filters is Result.Loading) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      CircularProgressIndicator()
    }
    return
  }
  AppTheme {
    Scaffold(
      topBar = {
        FilterTopBar(onBackPressed, onSaveFilter)
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .padding(innerPadding)
          .fillMaxSize()
      ) {
        ChipGroup(filters, selectedFilters, onClick)
        if (selectedFilters is Result.Loading)
          CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
          )
      }
    }
  }
}

@Composable
fun SearchFiltersScreen(viewModel: HomeFeedViewModel = hiltViewModel(), onBackPressed: () -> Unit) {
  var currSelectedFilters = remember { mutableListOf<Breed>() }
  val filters by viewModel.filters.observeAsState()
  val selectedFilters by viewModel.selectedFilters.observeAsState()
  val scope = rememberCoroutineScope()
  LaunchedEffect(selectedFilters) {
    if (selectedFilters is Result.Success) {
      currSelectedFilters = (selectedFilters as Result.Success).data as MutableList<Breed>
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
      }
      onBackPressed()
    })
}