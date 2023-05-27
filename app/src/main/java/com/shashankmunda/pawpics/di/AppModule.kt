package com.shashankmunda.pawpics.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import com.shashankmunda.pawpics.api.CatApiService
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
    fun getCacheDir(@ApplicationContext context: Context): Cache {
        return Cache(context.cacheDir,Utils.cacheSize)
    }
    @Singleton
    @Provides
    fun provideOkHttpObject(myCache: Cache,application:Application):OkHttpClient{
        return OkHttpClient.Builder()
            .readTimeout(30,TimeUnit.SECONDS)
            .connectTimeout(30,TimeUnit.SECONDS)
            .cache(myCache)
            .addInterceptor{ chain ->
                var request=chain.request()
                request= if(Utils.hasInternetConnection(application))
                 request.newBuilder().header("Cache-Control","public, max-age="+5).build()
                else request.newBuilder().header("Cache-Control","public, only-if-cached, max-stale="+60*60*24*7).build()
                chain.proceed(request)
            }
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
    fun provideNotificationManager(@ApplicationContext context:Context):NotificationManager{
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}