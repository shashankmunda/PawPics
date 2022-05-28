package com.example.pawpics.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object NetworkModule {
    private const val BASE_URL:String = "https://api.thecatapi.com/v1/"
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
    private val service:CatApiService by lazy{
        retrofit.create(CatApiService::class.java)
    }
    fun getCatService()= service
}
