package com.shashankmunda.pawpics.ui.feed

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shashankmunda.pawpics.base.BaseViewModel
import com.shashankmunda.pawpics.data.Cat
import com.shashankmunda.pawpics.repository.CatRepository
import com.shashankmunda.pawpics.util.ImageSize
import com.shashankmunda.pawpics.util.MimeType
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.Utils.BATCH_SIZE
import com.shashankmunda.pawpics.util.Utils.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeFeedViewModel @Inject constructor(private val catRepository: CatRepository, private val application: Application): BaseViewModel() {

    private var _cats= MutableLiveData<Result<List<Cat>>>()
    val cats: LiveData<Result<List<Cat>>>
        get()=_cats

    private var pageNo = 1

    init{
        fetchCatImages()
    }

     fun fetchCatImages() {
         pageNo++
        _cats.postValue(Result.Loading())
        if (hasInternetConnection(application))
            makeApiRequest()
        else
            _cats.postValue(Result.Error("No Internet connection"))
    }

    private fun makeApiRequest() {
        ioScope.launch{
            try {
                val catsList = catRepository.getCats(BATCH_SIZE, ImageSize.FULL, MimeType.PNG, pageNo)
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

    fun resetPageCount() {
        pageNo = 1
    }
}