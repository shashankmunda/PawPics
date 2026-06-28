package com.shashankmunda.pawpics.ui.filter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

import androidx.compose.ui.res.stringResource
import com.shashankmunda.pawpics.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterTopBar(onBackPressed: () -> Unit, onSaveFilter: () -> Unit) {
  TopAppBar(
    title = {},
    navigationIcon = {
      IconButton(onClick = onBackPressed) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = stringResource(R.string.cd_back),
        )
      }
    },
    actions = {
      IconButton(onClick = onSaveFilter) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = stringResource(R.string.cd_apply_filter)
        )
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.primary,
      navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
      actionIconContentColor = MaterialTheme.colorScheme.onPrimary
    )
  )
}