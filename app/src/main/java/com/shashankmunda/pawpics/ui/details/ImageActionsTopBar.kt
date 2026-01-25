package com.shashankmunda.pawpics.ui.details

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageActionsTopBar(areActionsEnabled: Boolean, onBackPressed: () -> Unit, onSave: () -> Unit, onShare: () -> Unit, onApplyAsWallpaper: () -> Unit) {
  TopAppBar(
    title = {
      // Your code here
    },
    navigationIcon = {
      IconButton(onClick=onBackPressed) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
        )
      }
    },
    actions = {
      IconButton(onClick=onSave, enabled= areActionsEnabled){
        Icon(
          imageVector = Icons.Default.Save,
          contentDescription = "Back"
        )
      }
      IconButton(onClick=onShare, enabled= areActionsEnabled){
        Icon(
          imageVector = Icons.Default.Share,
          contentDescription = "Back"
        )
      }
      IconButton(onClick=onApplyAsWallpaper, enabled= areActionsEnabled){
        Icon(
          imageVector = Icons.Default.Wallpaper,
          contentDescription = "Back"
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