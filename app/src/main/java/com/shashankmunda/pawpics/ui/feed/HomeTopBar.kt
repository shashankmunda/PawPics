package com.shashankmunda.pawpics.ui.feed

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.shashankmunda.pawpics.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar( onViewFilters: () -> Unit, drawerState: DrawerState) {
  val scope = rememberCoroutineScope()
  TopAppBar(
    title = {
      Text("", color = MaterialTheme.colorScheme.onPrimary)
    },
    navigationIcon = {
      IconButton(onClick = {
        scope.launch {
          drawerState.open()
        }
      }) {
        Icon(
          imageVector = Icons.Default.Menu,
          contentDescription = stringResource(R.string.cd_open_drawer),
          tint = MaterialTheme.colorScheme.onPrimary
        )
      }
    },
    actions = {
      IconButton(onClick = onViewFilters){
        Icon(
          imageVector = Icons.Default.FilterList,
          contentDescription = stringResource(R.string.cd_filters)
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