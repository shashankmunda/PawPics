package com.shashankmunda.pawpics.ui.fragments

import android.app.Application
import androidx.lifecycle.*
import com.shashankmunda.pawpics.base.BaseViewModel
import com.shashankmunda.pawpics.model.Cat
import com.shashankmunda.pawpics.repository.CatRepository
import com.shashankmunda.pawpics.util.ImageSize
import com.shashankmunda.pawpics.util.MimeType
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.Utils
import com.shashankmunda.pawpics.util.Utils.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(private val catRepository: CatRepository, private val application: Application): BaseViewModel() {

    private var _cats= MutableLiveData<Result<List<Cat>>>()
    val cats: LiveData<Result<List<Cat>>>
        get()=_cats

    private var _currCatStatus=MutableLiveData<Result<Cat>>()
    val currCatStatus:LiveData<Result<Cat>>
        get()=_currCatStatus

    private var catImageSpecs=HashMap<String,Cat>()

    init{
        fetchCatImages()
    }

    private fun fetchCatImages() {
        _cats.postValue(Result.Loading())
        if (hasInternetConnection(application)) {
            makeApiRequest()
        }
        else _cats.postValue(Result.Error("No Internet connection"))
    }

    private fun makeApiRequest() {
        ioScope.launch{
            try {
                val response = catRepository.getCats(Utils.OFFSET + Random.nextInt(Utils.MAX_LIMIT), ImageSize.THUMB,MimeType.PNG)
                _cats.postValue(updateCats(response))
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _cats.postValue(Result.Error("Couldn't connect to the source"))
                    else -> _cats.postValue(Result.Error("JSON parsing error"))
                }
            }
        }
    }

    private fun updateCats(response: Response<List<Cat>>): Result<List<Cat>> {
        if(response.isSuccessful && response.body()?.isNotEmpty()== true){
            val cats=response.body()!!.distinctBy { cat ->
                cat.id
            }
            return Result.Success(cats)
        }
        return Result.Error(response.message())
    }

    fun refreshCalled() = fetchCatImages()

    fun isCatSpecsAvailable(catImageId: String): Boolean {
        _currCatStatus.postValue(Result.Loading())
        return catImageSpecs.containsKey(catImageId)
    }

    fun getCatSpecs(catImageId: String): Cat? {
        return catImageSpecs[catImageId]
    }

    fun fetchCatSpecs(lifecycleScope:LifecycleCoroutineScope, catImageId: String) {
        if (!hasInternetConnection(application)) {
            _currCatStatus.postValue(Result.Error("Couldn't connect to the Internet. Please check your connection"))
        }
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = catRepository.getCatImage(catImageId, ImageSize.FULL)
                if (response.isSuccessful && response.body()!=null)
                {
                    _currCatStatus.postValue(Result.Success(response.body()!!))
                    catImageSpecs[catImageId] = response.body()!!
                }
                else _currCatStatus.postValue(Result.Error("Image not found"))
            } catch (exception: Exception) {
                when (exception) {
                    is IOException -> _currCatStatus.postValue(Result.Error("Couldn't connect to the API"))
                    else -> _currCatStatus.postValue(Result.Error("Problems parsing the JSON response"))
                }
            }
        }
    }

}