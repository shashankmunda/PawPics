package com.shashankmunda.pawpics.ui.filter

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shashankmunda.pawpics.data.Breed
import com.shashankmunda.pawpics.util.Result

@Composable
fun Chip(isSelected: Boolean, onClick: (Boolean, String) -> Unit, label: String) {
  val (internalSelected, setInternalSelected) = rememberSaveable { mutableStateOf(isSelected) }

  FilterChip(
    selected = internalSelected,
    onClick = {
      val newState = !internalSelected
      setInternalSelected(newState)
      onClick(newState, label)
    },
    label = {
      Text(label)
    },
    leadingIcon = if (internalSelected) {
      {
        Icon(
          Icons.Filled.Done,
          "Done icon",
          Modifier.size(FilterChipDefaults.IconSize)
        )
      }
    } else null,
    modifier = Modifier.padding(end = 8.dp)
  )
}

@Composable
fun ChipRow(
  items: List<Breed>,
  selectedFilters: List<Breed>?,
  onChipClick: (Boolean, String) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp)
  ) {
    items.forEach { item ->
      Chip(
        isSelected = selectedFilters?.contains(item) == true,
        onClick = onChipClick,
        label = item.name
      )
    }
  }
}

@Composable
fun ChipGroup( items: Result<List<Breed>>?, selectedFilters: Result<List<Breed>>?, onClick: (Boolean, String) -> Unit) {
  val chunkedItems = items?.data?.chunked(10)

  if (chunkedItems != null) {
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
    ) {
      items(chunkedItems.size) { index ->
        ChipRow(
          items = chunkedItems[index],
          selectedFilters = selectedFilters?.data,
          onChipClick = onClick
        )
      }
    }
  }
}