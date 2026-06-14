package com.shashankmunda.pawpics.ui.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.request.ImageResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.shashankmunda.pawpics.data.Cat
import com.shashankmunda.pawpics.util.Result

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