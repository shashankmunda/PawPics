package com.shashankmunda.pawpics.ui.feed

import android.app.Application
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.request.ImageResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.shashankmunda.pawpics.R
import com.shashankmunda.pawpics.data.Cat
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.Utils.hasInternetConnection
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeFeedContent(
  cats: Result<List<Pair<Cat, ImageResult>>>?,
  cachedCats: List<Pair<Cat, ImageResult>>?,
  loadMore: Boolean?,
  onViewFilters: () -> Unit,
  onClick: (Cat) -> Unit,
  onRefresh: () -> Unit,
  onLoadMore: () -> Unit,
  drawerState: DrawerState
) {

  Scaffold(
    topBar = {
      HomeTopBar(onViewFilters, drawerState)
    },
  ) { innnerPadding ->
    Column(
      modifier = Modifier
        .padding(innnerPadding)
        .fillMaxSize()
    ) {
      if (cats is Result.Loading && loadMore != true) {
        Box(modifier = Modifier.fillMaxSize()) {
          CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
          )
        }
        return@Column
      }
      if (cats is Result.Error && cachedCats == null) {
        HomeFeedError()
        return@Column
      }
      CatsStaggeredGrid(cats, cachedCats, onClick, onRefresh, onLoadMore)
      if (loadMore == true)
        CircularProgressIndicator(
          modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 8.dp, bottom = 8.dp),
        )
    }
  }
}

@Composable
private fun HomeFeedError() {
  val context = LocalContext.current
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Image(
      painter = painterResource(R.drawable.ic_baseline_broken_image_24),
      contentDescription = null,
      alignment = Alignment.Center
    )
    Text(
      text = "Something went wrong. Please check your internet connection and try again.",
      modifier = Modifier.padding(horizontal = 8.dp),
      textAlign = TextAlign.Center
    )
    Button(
      onClick = {
        if (!hasInternetConnection(context as Application)) {
          Intent(Settings.ACTION_WIRELESS_SETTINGS).apply {
            context.startActivity(this)
          }
        }
      },
      colors = ButtonDefaults.buttonColors(
        containerColor = Color.Gray,
        contentColor = Color.Black
      ),
    ) {
      Icon(imageVector = Icons.Default.Settings, "Open settings")
      Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
      Text("Open settings")
    }
  }
}

@Composable
fun HomeFeedScreen(
  viewModel: HomeFeedViewModel,
  onViewFilters: () -> Unit,
  onClick: (Cat) -> Unit,
  drawerState: DrawerState
) {
  val loadMore by viewModel.loadMore.observeAsState()
  val cats by viewModel.cats.observeAsState()
  val cachedCats by viewModel.cachedCats.observeAsState()
  val lifecycleOwner = LocalLifecycleOwner.current
  val scope = rememberCoroutineScope()
  val context = LocalContext.current
  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {

        if (viewModel.getCachedCats()?.isNotEmpty() == true) {
          viewModel.restoreData()
        }
      }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observer)
    }
  }

  LaunchedEffect(cats) {
    when (cats) {
      is Result.Success -> viewModel.setLoadMore(false)
      is Result.Error -> {
        viewModel.setLoadMore(false)
        Toast.makeText(context, (cats as Result.Error).message, Toast.LENGTH_SHORT).show()
      }

      else -> {}
    }
  }
  HomeFeedContent(
    cats, cachedCats, loadMore,
    onViewFilters,
    onClick,
    {
      scope.launch {
        viewModel.resetPageCount()
        viewModel.fetchCatImages()
      }
    }, {
      scope.launch {
        viewModel.loadMoreCats()
      }
    }, drawerState
  )
}
