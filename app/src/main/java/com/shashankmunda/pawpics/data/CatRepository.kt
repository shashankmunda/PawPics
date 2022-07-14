package com.shashankmunda.pawpics.data

import com.shashankmunda.pawpics.network.CatApiService
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class CatRepository @Inject constructor(private var catApiService: CatApiService) {
    suspend fun getImages(limit:Int)=
       catApiService.getImages(limit)
}