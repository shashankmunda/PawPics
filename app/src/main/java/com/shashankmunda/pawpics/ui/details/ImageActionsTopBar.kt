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

import androidx.compose.ui.res.stringResource
import com.shashankmunda.pawpics.R

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
          contentDescription = stringResource(R.string.cd_back),
        )
      }
    },
    actions = {
      IconButton(onClick=onSave, enabled= areActionsEnabled){
        Icon(
          imageVector = Icons.Default.Save,
          contentDescription = stringResource(R.string.cd_save)
        )
      }
      IconButton(onClick=onShare, enabled= areActionsEnabled){
        Icon(
          imageVector = Icons.Default.Share,
          contentDescription = stringResource(R.string.cd_share)
        )
      }
      IconButton(onClick=onApplyAsWallpaper, enabled= areActionsEnabled){
        Icon(
          imageVector = Icons.Default.Wallpaper,
          contentDescription = stringResource(R.string.cd_wallpaper)
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