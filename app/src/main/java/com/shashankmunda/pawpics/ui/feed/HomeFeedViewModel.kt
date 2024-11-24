package com.shashankmunda.pawpics.ui.feed

import android.app.Application
import android.graphics.Bitmap.Config
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.ImageResult
import com.shashankmunda.pawpics.base.BaseViewModel
import com.shashankmunda.pawpics.data.Breed
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

    private var _cats= MutableLiveData<Result<List<Pair<Cat,ImageResult>>>>()
    val cats: LiveData<Result<List<Pair<Cat,ImageResult>>>>
        get()=_cats

    private var _filters= MutableLiveData<Result<List<Breed>>>()
    val filters: LiveData<Result<List<Breed>>>
        get()=_filters

    private var _selectedFilters = MutableLiveData<Result<List<Breed>>>()
    val selectedFilters: LiveData<Result<List<Breed>>>
        get() = _selectedFilters

    private var pageNo = 0
    private var imageLoader = application.imageLoader

    init{
        fetchCatImages()
        fetchFilters()
    }

    fun setFiltersForSelected(filters: List<Breed>) {
        _selectedFilters.postValue(Result.Success(filters))
    }

    fun addFilterToSelection(filter: Breed) {
        if(_selectedFilters.isInitialized)
        _selectedFilters.postValue(Result.Success(_selectedFilters.value?.data?.plus(filter)!!))
        else _selectedFilters.postValue(Result.Success(listOf(filter)))
    }

    fun removeFilterFromSelection(filter: Breed) {
        _selectedFilters.value = Result.Success(_selectedFilters.value?.data?.filterNot { it == filter }!!)
    }

    private fun fetchFilters(){
        _filters.postValue(Result.Loading())
        if(hasInternetConnection(application)){
            ioScope.launch {
                try {
                    val filters = catRepository.fetchFilters()
                    if(!filters.isNullOrEmpty())
                        _filters.postValue(Result.Success(filters))
                    else
                        _filters.postValue(Result.Error("Error fetching filters"))
                }
                catch (t: Throwable) {
                    _filters.postValue(Result.Error("Error fetching filters"))
                }
            }
        }
        else
            _cats.postValue(Result.Error("No Internet connection"))
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
                val catsList = catRepository.getCats(BATCH_SIZE, ImageSize.FULL, MimeType.PNG, pageNo,
                    _selectedFilters.value?.data?.joinToString(",") { it.id })
                val results=mutableListOf<Pair<Cat,ImageResult>>()
                if(catsList != null){
                    for(cat in catsList){
                        val result = imageLoader.execute(
                            ImageRequest.Builder(application)
                                .data(cat.url)
                                .bitmapConfig(Config.ALPHA_8)
                                .build())
                        if(result.drawable!=null)
                            results.add(Pair(cat,result))
                    }
                }
                if(results.isNotEmpty())
                    _cats.postValue(Result.Success(results))
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
        pageNo = 0
    }
}