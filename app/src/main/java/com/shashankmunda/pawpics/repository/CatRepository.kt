package com.shashankmunda.pawpics.repository

import com.shashankmunda.pawpics.api.CatApiService
import com.shashankmunda.pawpics.util.ImageSize
import com.shashankmunda.pawpics.util.MimeType
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class CatRepository @Inject constructor(private var catApiService: CatApiService) {
    suspend fun getCats(limit:Int,size:ImageSize,mimeType:MimeType)=
       catApiService.getCats(limit,size.toString().lowercase(),mimeType.toString().lowercase())
    suspend fun getCatImage(imageId:String,size:ImageSize)=
        catApiService.getCat(imageId,size.toString().lowercase())
}