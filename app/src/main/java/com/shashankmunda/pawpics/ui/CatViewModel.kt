package com.shashankmunda.pawpics.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashankmunda.pawpics.repository.CatRepository
import com.shashankmunda.pawpics.model.Cat
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.Utils.Companion.MAX_LIMIT
import com.shashankmunda.pawpics.util.Utils.Companion.OFFSET
import com.shashankmunda.pawpics.util.Utils.Companion.clearImageCache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CatViewModel @Inject constructor(private val catRepository: CatRepository, private val application: Application): ViewModel() {
    private val _cats= MutableLiveData<Result<List<Cat>>>()
    val cats: LiveData<Result<List<Cat>>>
        get()=_cats
    init{
        fetchNewCats()
    }
    @UiThread
     private fun fetchNewCats() {
        _cats.postValue(Result.Loading())
            if (hasInternetConnection()) {
                viewModelScope.launch(Dispatchers.IO){
                    try {
                        val response = catRepository.getCats(OFFSET + Random.nextInt(MAX_LIMIT),"thumb")
                        _cats.postValue(updateCats(response))
                    }
                    catch(t:Throwable){
                        when(t){
                            is IOException -> _cats.postValue(Result.Error("Couldn't connect to the source"))
                            else -> _cats.postValue(Result.Error("JSON parsing error"))
                        }
                    }
            }
        } else _cats.postValue(Result.Error("No Internet connection"))
     }

    private fun updateCats(response: Response<List<Cat>>): Result<List<Cat>>? {
        if(response.isSuccessful && response.body()?.isNotEmpty()== true){
            val cats=response.body()!!.distinctBy { cat ->
                cat.id
            }
            clearImageCache(application)
            return Result.Success(cats)
        }
        return Result.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager= application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            val networkInfo=connectivityManager.activeNetwork ?: return false
            val capabilities=connectivityManager.getNetworkCapabilities(networkInfo)?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        connectivityManager.activeNetworkInfo?.run{
            return when(type){
                TYPE_WIFI -> true
                TYPE_MOBILE -> true
                TYPE_ETHERNET -> true
                else -> false
            }
        }
        return false
    }

    fun refreshCalled(){
         fetchNewCats()
     }
}