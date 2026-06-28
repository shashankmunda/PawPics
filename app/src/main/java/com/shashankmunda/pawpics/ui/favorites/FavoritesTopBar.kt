package com.shashankmunda.pawpics.ui.favorites

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
fun FavoritesTopBar(onBackPressed: () -> Unit) {
  TopAppBar(
    title ={

    },
    navigationIcon = {
      IconButton(onClick=onBackPressed) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = stringResource(R.string.cd_back),
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