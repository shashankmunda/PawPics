package com.shashankmunda.pawpics.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.ImageLoader
import coil.request.ImageRequest
import coil3.ImageLoader
import coil3.request.ImageRequest
import com.shashankmunda.pawpics.base.BaseViewModel
import com.shashankmunda.pawpics.data.Cat
import com.shashankmunda.pawpics.repository.CatRepository
import com.shashankmunda.pawpics.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatImageViewModel @Inject constructor(private val catRepository: CatRepository): BaseViewModel() {
    @Inject lateinit var imageRequest: ImageRequest.Builder
    @Inject lateinit var imageLoader : ImageLoader
    private var _currCatStatus= MutableLiveData<Result<Cat>>()
    val currCatStatus: LiveData<Result<Cat>>
        get()=_currCatStatus

    fun fetchCatSpecs(catImageId: String) {
        ioScope.launch{
            val catDetails = catRepository.getCatImageDetails(catImageId)
            if(catDetails==null)
                _currCatStatus.postValue(Result.Error("Image not found"))
            else _currCatStatus.postValue(Result.Success(catDetails))
        }
    }
}