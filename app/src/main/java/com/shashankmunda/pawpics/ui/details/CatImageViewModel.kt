package com.shashankmunda.pawpics.ui.details

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil3.ImageLoader
import coil3.request.ImageRequest
import com.shashankmunda.pawpics.base.BaseViewModel
import com.shashankmunda.pawpics.data.Cat
import com.shashankmunda.pawpics.repository.CatRepository
import com.shashankmunda.pawpics.util.FileUtils
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class CatImageViewModel @Inject constructor(private val catRepository: CatRepository, private val application: Application): BaseViewModel() {
    @Inject lateinit var imageRequest: ImageRequest.Builder
    @Inject lateinit var imageLoader : ImageLoader
    private var _currCatStatus= MutableLiveData<Result<Cat>>()
    val currCatStatus: LiveData<Result<Cat>>
        get()=_currCatStatus

    private var _areActionsEnabled = MutableLiveData(false)
    val areActionsEnabled: LiveData<Boolean>
        get() = _areActionsEnabled

    fun fetchCatSpecs(catImageId: String) {
        ioScope.launch{
            val catDetails = catRepository.getCatImageDetails(catImageId)
            if(catDetails==null)
                _currCatStatus.postValue(Result.Error("Image not found"))
            else _currCatStatus.postValue(Result.Success(catDetails))
        }
    }

    fun enableActions(enable: Boolean){
        _areActionsEnabled.postValue(enable)
    }

    fun downloadAndShareGif(context: Context, imageId: String, fileExt: String) {
      val url = "https://cdn2.thecatapi.com/images/$imageId.$fileExt"
      ioScope.launch {
        val responseStream = catRepository.downloadUrl(url)
        responseStream.use { input ->
          val fileUri = FileUtils.getFileFromExternalCache(imageId, context, fileExt)
          FileOutputStream(fileUri).use { output ->
            input?.copyTo(output)
          }
        }
        val contentUri = FileUtils.getCurrentImageUri(imageId, context, fileExt)
        Utils.shareContent(context, contentUri, fileExt)
      }
    }
}