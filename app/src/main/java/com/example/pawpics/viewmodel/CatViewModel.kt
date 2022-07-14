package com.example.pawpics.viewmodel

import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawpics.data.CatRepository
import com.example.pawpics.model.Cat
import com.example.pawpics.util.Constants.Companion.MAX_LIMIT
import com.example.pawpics.util.Constants.Companion.OFFSET
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CatViewModel @Inject constructor(private val catRepository: CatRepository): ViewModel() {
    private val _cats= MutableLiveData<List<Cat>>()
    val cats: LiveData<List<Cat>>
        get()=_cats
    init{
        fetchData()
    }
    @UiThread
     private fun fetchData() {
        viewModelScope.launch {
            val response = catRepository.getImages(OFFSET+Random.nextInt(MAX_LIMIT))
            if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                filterDuplicateOrInvalidEntries()
                _cats.postValue(response.body())
            }
        }
     }

    private fun filterDuplicateOrInvalidEntries() {
        _cats.value?.distinctBy { cat->
            cat.url
        }
    }

    fun refreshCalled(){
         fetchData()
     }
}