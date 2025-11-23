package com.shashankmunda.pawpics.ui.details

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingButton(onClick: () -> Unit, imageVector: ImageVector, contentDescription: String?, containerColor: Color = MaterialTheme.colorScheme.primary, contentColor: Color = MaterialTheme.colorScheme.onPrimary) {
  FloatingActionButton(
    onClick = onClick,
    containerColor = containerColor,
    contentColor = contentColor,
    shape = CircleShape
  ) {
    Icon(imageVector = imageVector, contentDescription = contentDescription)
  }
}