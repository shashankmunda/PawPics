package com.shashankmunda.pawpics.api

import com.shashankmunda.pawpics.data.Breed
import com.shashankmunda.pawpics.data.Cat
import com.shashankmunda.pawpics.data.CatDetails
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface CatApiService {

    @GET("images/search")
    suspend fun getCats(@Query("limit") limit:Int, @Query("size") size:String, @Query("mime_types") mimeType:String, @Query("page") page:Int, @Query("breed_ids") breedIds: String?): Response<List<Cat>>

    @GET("images/{image_id}")
    suspend fun getCat(@Path("image_id") imageId:String):Response<CatDetails>

    @GET("breeds")
    suspend fun fetchBreeds(): Response<List<Breed>>

    @Streaming
    @GET
      fun downloadFile(@Url url: String): Call<ResponseBody>

}