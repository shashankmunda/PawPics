package com.shashankmunda.pawpics.di

import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import com.shashankmunda.pawpics.api.CatApiService
import com.shashankmunda.pawpics.data.CatsDatabase
import com.shashankmunda.pawpics.util.Utils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun getCacheDir(@ApplicationContext context: Context) =
        Cache(context.cacheDir,Utils.cacheSize)

    @Singleton
    @Provides
    fun provideOkHttpObject(tokenInterceptor: TokenInterceptor):OkHttpClient{
        return OkHttpClient.Builder()
            .readTimeout(30,TimeUnit.SECONDS)
            .connectTimeout(30,TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideCatService(okHttpClient: OkHttpClient): CatApiService {
        return Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(CatApiService::class.java)
    }
    @Singleton
    @Provides
    fun provideNotificationManager(@ApplicationContext context:Context) =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Singleton
    @Provides
    fun provideCatDatabase(@ApplicationContext context:Context) = Room.databaseBuilder(context,CatsDatabase::class.java, "cats_db")
        .build()

    @Singleton
    @Provides
    fun provideCatDao(catsDb: CatsDatabase) = catsDb.getCatsDao()
}