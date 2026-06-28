package com.shashankmunda.pawpics.ui.feed

import android.app.Application
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shashankmunda.pawpics.R
import com.shashankmunda.pawpics.util.Utils

@Composable
fun HomeFeedError() {
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
      text = stringResource(R.string.check_your_internet_connection),
      modifier = Modifier.padding(horizontal = 8.dp),
      textAlign = TextAlign.Center
    )
    Button(
      onClick = {
        if (!Utils.hasInternetConnection(context as Application)) {
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
      Icon(imageVector = Icons.Default.Settings, stringResource(R.string.open_settings))
      Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
      Text(stringResource(R.string.open_settings))
    }
  }
}