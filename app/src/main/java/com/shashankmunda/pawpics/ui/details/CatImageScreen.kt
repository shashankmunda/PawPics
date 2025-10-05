package com.shashankmunda.pawpics.ui.details

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.request.SuccessResult
import coil3.toBitmap
import com.shashankmunda.pawpics.ui.AppTheme
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.Utils
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CatImageContent(
  imageId: String,
  viewModel: CatImageViewModel,
  onBackPressed: () -> Unit,
  onShare: () -> Unit,
  onSetWallpaper: () -> Unit,
  onDownload: () -> Unit,
  onSuccess: (result: SuccessResult) -> Unit,
  onFabClick: () -> Unit
) {
  val currStatus by viewModel.currCatStatus.observeAsState()
  val areActionsEnabled by viewModel.areActionsEnabled.observeAsState()
  LaunchedEffect(true) {
    viewModel.fetchCatSpecs(imageId)
  }
  AppTheme {
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
      floatingActionButton = {
        if (currStatus is Result.Error)
          FloatingButton(onFabClick)
      }
    ) { innerPadding ->
      Column(
        modifier = Modifier
          .padding(innerPadding)
          .fillMaxSize()
      ) {
        CatImageView(currStatus, onSuccess)
      }
    }
  }
}

@Composable
fun CatImageScreen(
  imageId: String,
  fileExt: String,
  viewModel: CatImageViewModel = hiltViewModel(),
  onBackPressed: () -> Unit
) {
  val context = LocalContext.current
  var catBitmap by remember { mutableStateOf<Bitmap?>(null) }
  val scope = rememberCoroutineScope()
  CatImageContent(
    imageId,
    viewModel,
    onBackPressed,
    {
      scope.launch {
        Utils.shareImage(context, catBitmap!!, imageId, fileExt)
      }
    },
    {
      scope.launch {
        WallpaperManager.getInstance(context).setBitmap(catBitmap)
      }
    },
    {
      scope.launch {
        Utils.saveImage(context, imageId, fileExt)
      }
    },
    { it ->
      viewModel.enableActions(true)
      viewModel.currCatStatus.value?.data?.let { it1 ->
        catBitmap = it.image.toBitmap(it1.width, it1.height, ARGB_8888)
      }
    },
    { scope.launch { viewModel.fetchCatSpecs(imageId) } }
  )
}