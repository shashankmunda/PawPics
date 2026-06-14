package com.shashankmunda.pawpics.ui.feed

import android.widget.Toast
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.shashankmunda.pawpics.data.Cat
import com.shashankmunda.pawpics.util.Result
import kotlinx.coroutines.launch

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
