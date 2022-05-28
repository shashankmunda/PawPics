package com.example.pawpics.network

import com.example.pawpics.model.Cat
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CatApiService {
    @GET("images/search")
    suspend fun getImages(@Query("limit") limit:Int): Response<List<Cat>>
}