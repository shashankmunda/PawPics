package com.shashankmunda.pawpics.ui.filter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.shashankmunda.pawpics.data.Breed
import com.shashankmunda.pawpics.util.Result

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