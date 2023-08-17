package com.shashankmunda.pawpics.di

import com.example.pawpics.BuildConfig
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(): Interceptor {
  override fun intercept(chain: Chain): Response {
    val request= chain.request()
      .newBuilder()
      .addHeader("x-api-key",BuildConfig.CAT_API_KEY)
      .build()
    return chain.proceed(request)
  }
}