package com.example.pawpics.di

import com.example.pawpics.network.CatApiService
import com.example.pawpics.repository.CatRepository
import com.example.pawpics.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object NetworkModule {
    @ViewModelScoped
    @Provides
    fun provideCatService(): CatApiService {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(CatApiService::class.java)
    }
    @ViewModelScoped
    @Provides
    fun provideCatRepo(catApiService: CatApiService):CatRepository{
        return CatRepository(catApiService)
    }
}