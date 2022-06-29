package com.example.pawpics.repository

import com.example.pawpics.network.CatApiService
import javax.inject.Inject

class CatRepository @Inject constructor(private val catApiService:CatApiService) {
   suspend fun getImages(limit:Int)=
       catApiService.getImages(limit)
}