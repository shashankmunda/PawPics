package com.shashankmunda.pawpics.ui.details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.request.SuccessResult
import com.shashankmunda.pawpics.data.Cat
import com.shashankmunda.pawpics.util.Result

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CatImageContent(
  currStatus:  Result<Cat>?,
  areActionsEnabled: Boolean?,
  onBackPressed: () -> Unit,
  onShare: () -> Unit,
  onSetWallpaper: () -> Unit,
  onDownload: () -> Unit,
  onSuccess: (result: SuccessResult) -> Unit,
  onFabClick: () -> Unit
) {
  Scaffold(
    topBar = {
      ImageActionsTopBar(
        areActionsEnabled == true,
        onBackPressed,
        onDownload,
        onShare,
        onSetWallpaper
      )
    },
  ) { innerPadding ->
    Column(
      modifier = Modifier.Companion
        .padding(innerPadding)
        .fillMaxSize(),
      verticalArrangement = Arrangement.Center
    ) {
      CatImageView(currStatus, onSuccess)
    }
  }
}