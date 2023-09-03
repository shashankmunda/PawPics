package com.shashankmunda.pawpics.api

import com.shashankmunda.pawpics.data.Cat
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatApiService {

    @GET("images/search")
    suspend fun getCats(@Query("limit") limit:Int, @Query("size") size:String, @Query("mime_types") mimeType:String, @Query("page") page:Int): Response<List<Cat>>

    @GET("images/{image_id}")
    suspend fun getCat(@Path("image_id") imageId:String,@Query("size") size:String):Response<Cat>
}