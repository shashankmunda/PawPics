package com.shashankmunda.pawpics.ui.feed

import android.app.Application
import android.graphics.Bitmap.Config
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil3.imageLoader
import coil3.request.CachePolicy.ENABLED
import coil3.request.ImageRequest
import coil3.request.ImageResult
import coil3.request.allowHardware
import coil3.request.bitmapConfig
import com.shashankmunda.pawpics.R
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
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeFeedViewModel @Inject constructor(private val catRepository: CatRepository, private val application: Application): BaseViewModel() {

    private var _cats= MutableLiveData<Result<List<Pair<Cat,ImageResult>>>>()
    val cats: LiveData<Result<List<Pair<Cat, ImageResult>>>>
        get()=_cats

    private val _cachedCats = MutableLiveData<List<Pair<Cat, ImageResult>>>()
    val cachedCats: LiveData<List<Pair<Cat, ImageResult>>>
        get() = _cachedCats

    private var _filters= MutableLiveData<Result<List<Breed>>>()
    val filters: LiveData<Result<List<Breed>>>
        get()=_filters

    private var _selectedFilters = MutableLiveData<Result<List<Breed>>>(Result.Success(emptyList()))
    val selectedFilters: LiveData<Result<List<Breed>>>
        get() = _selectedFilters

    private val _themeChanged = MutableLiveData<Boolean>()
    val themeChanged: LiveData<Boolean> get() = _themeChanged

    private val _loadMoreOnScroll = MutableLiveData<Boolean>(false)
    val loadMore: LiveData<Boolean>
        get() = _loadMoreOnScroll

    private var pageNo = 0
    private var imageLoader = application.imageLoader

    init{
        fetchCatImages()
        fetchFilters()
    }

    fun restoreData() {
        if (_cachedCats.value?.isNotEmpty() == true) {
            _cats.postValue(Result.Success(null))
        }
    }

    fun getCachedCats(): List<Pair<Cat, ImageResult>>? {
        return _cachedCats.value?.toList()
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
                        _filters.postValue(Result.Error(application.getString(R.string.error_fetching_filters)))
                }
                catch (t: Throwable) {
                    _filters.postValue(Result.Error(application.getString(R.string.error_fetching_filters)))
                }
            }
        }
        else
            _cats.postValue(Result.Error(application.getString(R.string.no_internet_connection)))
    }

     fun fetchCatImages() {
        _cats.postValue(Result.Loading())
        if (hasInternetConnection(application))
            makeApiRequest()
        else
            _cats.postValue(Result.Error(application.getString(R.string.no_internet_connection)))
    }

    private fun makeApiRequest() {
        ioScope.launch{
            try {
                val catsList = catRepository.getCats(BATCH_SIZE, ImageSize.FULL, listOf(MimeType.PNG, MimeType.GIF), pageNo,
                    _selectedFilters.value?.data?.joinToString(",") { it.id })
                var results=mutableListOf<Pair<Cat,ImageResult>>()
                if(catsList != null){
                    supervisorScope {
                        val imageJobs = catsList.map { cat ->
                            Pair(cat, async{
                                return@async imageLoader.execute(
                                    ImageRequest.Builder(application)
                                        .data(cat.url)
                                        .memoryCachePolicy(ENABLED)
                                        .diskCachePolicy(ENABLED)
                                        .bitmapConfig(Config.ALPHA_8)
                                        .allowHardware(false)
                                        .build())
                            })
                        }
                        results = imageJobs.map{ imageJob ->
                          Pair(imageJob.first,runCatching{ imageJob.second.await()}.getOrNull())
                        }.filter { result -> result.second?.image != null } as MutableList<Pair<Cat, ImageResult>>
                    }
                }

                if(results.isNotEmpty())
                {
                    val currentCachedCats = _cachedCats.value ?: emptyList()
                    val updatedCachedCats = currentCachedCats + results
                    _cachedCats.postValue(updatedCachedCats)
                    pageNo++
                    _cats.postValue(Result.Success(null))
                }
                else
                    _cats.postValue(Result.Error(application.getString(R.string.error_fetching_cats)))
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _cats.postValue(Result.Error(application.getString(R.string.couldn_t_connect_to_the_source)))
                    else -> _cats.postValue(Result.Error(application.getString(R.string.json_parsing_error)))
                }
            }
        }
    }

    fun resetPageCount() {
        pageNo = 0
        _cachedCats.postValue(emptyList())
    }

    fun loadMoreCats() {
        if(_loadMoreOnScroll.value == true)
            return;
        _loadMoreOnScroll.postValue(true)
        fetchCatImages()
    }

    fun setLoadMore(loadMore: Boolean) {
        _loadMoreOnScroll.postValue(loadMore);
    }
}