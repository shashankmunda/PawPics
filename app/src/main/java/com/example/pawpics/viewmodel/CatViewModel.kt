package com.example.pawpics.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawpics.model.Cat
import com.example.pawpics.repository.CatRepository
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
        Log.d("ViewModel initialized","YES")
        fetchData()
    }
     fun fetchData() {
        viewModelScope.launch {
            val response = catRepository.getImages(Random.nextInt(100))
            if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                _cats.value?.filter { cat ->
                    cat.height!=null && cat.width!=null
                }
                _cats.value=response.body()
            }
        }
    }
}