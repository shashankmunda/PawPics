package com.shashankmunda.pawpics.ui.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageResult
import com.shashankmunda.pawpics.data.Cat
import com.shashankmunda.pawpics.util.Result
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatsStaggeredGrid(
  cats: Result<List<Pair<Cat, ImageResult>>>?,
  cachedCats: List<Pair<Cat, ImageResult>>?,
  onClick: (Cat) -> Unit,
  onRefreshing: () -> Unit,
  onLoadMore: () -> Unit
) {
  var isPullToRefreshLoading by rememberSaveable { mutableStateOf(false) }
  val pullToRefreshState = rememberPullToRefreshState()

  // Only show pull-to-refresh loading when it's explicitly triggered by user
  // Not during initial loading
  val shouldShowPullToRefreshIndicator = isPullToRefreshLoading

  PullToRefreshBox(
    state = pullToRefreshState,
    isRefreshing = shouldShowPullToRefreshIndicator,
    onRefresh = {
      isPullToRefreshLoading = true
      onRefreshing()
    }
  ) {
    val catsResult = cats
    val gridState = rememberLazyStaggeredGridState()

    val shouldLoadMoreCats by remember {
      derivedStateOf {
        val layoutInfo = gridState.layoutInfo
        val totalItemsCount = layoutInfo.totalItemsCount
        val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()

        totalItemsCount > 0 &&
          lastVisibleItem != null &&
          lastVisibleItem.index >= totalItemsCount - 3
      }
    }
    val totalItems = remember {
      derivedStateOf {
        gridState.layoutInfo
      }
    }.value.totalItemsCount

    LaunchedEffect(shouldLoadMoreCats, totalItems) {
      if (shouldLoadMoreCats && catsResult !is Result.Loading) {
        onLoadMore()
      }
    }

    if(cachedCats != null && cachedCats.isNotEmpty()) {
      LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        state = gridState,
      ) {
        itemsIndexed(cachedCats) { index, item ->
          CatImageHolder(item, onClick)
        }
      }
    }
    when (catsResult) {
      is Result.Success, is Result.Error -> {
        // Reset pull-to-refresh loading when we get successful data
        if (isPullToRefreshLoading) {
          isPullToRefreshLoading = false
        }

      }

      is Result.Loading -> {
        // Don't show pull-to-refresh indicator for initial loading
        // Only show it if user explicitly triggered refresh
      }
      else -> {
        if (isPullToRefreshLoading) {
          isPullToRefreshLoading = false
        }
      }
    }
  }
}

@Composable
fun CatImageHolder(item: Pair<Cat, ImageResult>, onClick: (Cat) -> Unit) {
  val screenWidth = LocalConfiguration.current.screenWidthDp
  val height = remember {
    (1.0f * (item.first.height) * (screenWidth / 2)) / item.first.width
  }
  AsyncImage(
    model = item.first.url,
    contentDescription = null,
    modifier = Modifier
      .width(width = (screenWidth / 2 - 8).dp)
      .height(height = height.roundToInt().dp)
      .clickable { onClick(item.first) }
      .clip(RoundedCornerShape(16.dp))
  )
}