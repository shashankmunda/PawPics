package com.shashankmunda.pawpics.ui.fragments

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shashankmunda.pawpics.base.BaseViewModel
import com.shashankmunda.pawpics.model.Cat
import com.shashankmunda.pawpics.repository.CatRepository
import com.shashankmunda.pawpics.util.ImageSize
import com.shashankmunda.pawpics.util.MimeType
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.Utils.MAX_LIMIT
import com.shashankmunda.pawpics.util.Utils.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val catRepository: CatRepository, private val application: Application): BaseViewModel() {

    private var _cats= MutableLiveData<Result<List<Cat>>>()
    val cats: LiveData<Result<List<Cat>>>
        get()=_cats

    private var _currCatStatus=MutableLiveData<Result<Cat>>()
    val currCatStatus:LiveData<Result<Cat>>
        get()=_currCatStatus

    init{
        fetchCatImages()
    }

     fun fetchCatImages() {
        _cats.postValue(Result.Loading())
        if (hasInternetConnection(application))
            makeApiRequest()
        else
            _cats.postValue(Result.Error("No Internet connection"))
    }

    private fun makeApiRequest() {
        ioScope.launch{
            try {
                val catsList = catRepository.getCats(MAX_LIMIT, ImageSize.FULL,MimeType.PNG)
                if(catsList!=null)
                    _cats.postValue(Result.Success(catsList))
                else
                    _cats.postValue(Result.Error("Error fetching cats"))
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _cats.postValue(Result.Error("Couldn't connect to the source"))
                    else -> _cats.postValue(Result.Error("JSON parsing error"))
                }
            }
        }
    }

    fun fetchCatSpecs(catImageId: String) {
       ioScope.launch{
           val catDetails = catRepository.getCatImageDetails(catImageId)
           if(catDetails==null)
               _currCatStatus.postValue(Result.Error("Image not found"))
           else _currCatStatus.postValue(Result.Success(catDetails))
       }
    }

}