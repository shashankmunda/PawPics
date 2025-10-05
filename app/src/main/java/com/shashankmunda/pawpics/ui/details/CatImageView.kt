package com.shashankmunda.pawpics.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.SuccessResult
import com.shashankmunda.pawpics.R
import com.shashankmunda.pawpics.data.Cat
import com.shashankmunda.pawpics.util.Result

@Composable
fun CatImageView(currStatus: Result<Cat>?, onSuccess: (SuccessResult) -> Unit) {
  val height = LocalConfiguration.current.screenHeightDp.dp
  //(1.0f * displayMetrics.widthPixels * cat.height!!).toInt() / cat.width!!
  if(currStatus?.data?.url != null){
    val painter = rememberAsyncImagePainter(currStatus.data.url)
    val state by painter.state.collectAsState()
    when(state){
      is AsyncImagePainter.State.Empty,
        is AsyncImagePainter.State.Loading -> {
        CircularProgressIndicator()
      }
      is AsyncImagePainter.State.Success -> {
        onSuccess((state as AsyncImagePainter.State.Success).result)
        Image(
          painter = painter,
          contentDescription = null,
          modifier = Modifier.fillMaxWidth().height(height* currStatus.data.height / currStatus?.data?.width!!),
          contentScale = ContentScale.FillWidth
        )
      }
      is AsyncImagePainter.State.Error -> {
        Image(
          painter = painterResource(R.drawable.ic_baseline_broken_image_24),
          contentDescription = null,
          modifier = Modifier.fillMaxWidth().height(height* currStatus.data.height / currStatus?.data?.width!!),
          contentScale = ContentScale.FillWidth
        )
      }
    }
  }
}