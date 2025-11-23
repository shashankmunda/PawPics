package com.shashankmunda.pawpics.ui.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FavoritesScreen(onBackPressed: () -> Unit) {
  Scaffold(
    topBar = { FavoritesTopBar(onBackPressed = onBackPressed) }
  ){ innnerPadding ->
    Column(
      modifier = Modifier
        .padding(innnerPadding)
        .fillMaxSize()
    ) {
    }
  }
}