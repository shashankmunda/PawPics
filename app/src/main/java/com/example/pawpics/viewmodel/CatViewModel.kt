package com.example.pawpics.viewmodel

import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawpics.data.CatRepository
import com.example.pawpics.model.Cat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CatViewModel @Inject constructor(private val catRepository: CatRepository): ViewModel() {
    private val _cats= MutableLiveData<List<Cat>>()
    val cats: LiveData<List<Cat>>
        get()=_cats

    @UiThread
     private fun fetchData() {
        viewModelScope.launch {
            val response = catRepository.getImages(Random.nextInt(100))
            if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                filterDuplicateOrInvalidEntries()
                _cats.postValue(response.body())
            }
        }
     }

    private fun filterDuplicateOrInvalidEntries() {
        val catEntries = mutableSetOf<String>()
        _cats.value?.filter { cat ->
            var isNotDuplicateOrInvalid = false
            if (cat.height != null && cat.width != null && !catEntries.contains(cat.id)) {
                catEntries.add(cat.id)
                isNotDuplicateOrInvalid = true
            }
            isNotDuplicateOrInvalid
        }
    }

    fun refreshCalled(){
         fetchData()
     }
}