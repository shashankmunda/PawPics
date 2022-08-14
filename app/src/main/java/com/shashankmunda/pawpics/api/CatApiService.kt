package com.shashankmunda.pawpics.api

import com.example.pawpics.BuildConfig
import com.shashankmunda.pawpics.model.Cat
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface CatApiService {

    @Headers("x-api-key: ${BuildConfig.CAT_API_KEY}")
    @GET("images/search")
    suspend fun getCats(@Query("limit") limit:Int, @Query("size") size:String): Response<List<Cat>>

    @Headers("x-api-key: ${BuildConfig.CAT_API_KEY}")
    @GET("images/{image_id}")
    suspend fun getCat(@Path("image_id") imageId:String,@Query("size") size:String):Response<Cat>
}