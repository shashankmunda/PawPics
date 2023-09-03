package com.shashankmunda.pawpics.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shashankmunda.pawpics.base.BaseViewModel
import com.shashankmunda.pawpics.data.Cat
import com.shashankmunda.pawpics.repository.CatRepository
import com.shashankmunda.pawpics.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatImageViewModel @Inject constructor(private val catRepository: CatRepository): BaseViewModel() {

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