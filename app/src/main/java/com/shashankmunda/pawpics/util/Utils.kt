package com.shashankmunda.pawpics.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class Utils{
    companion object {
        const val BASE_URL:String = "https://api.thecatapi.com/v1/"
        const val OFFSET=30
        const val MAX_LIMIT=20
        const val cacheSize=(5*1024*1024).toLong()
        @OptIn(ExperimentalCoilApi::class)
        fun clearImageCache(context: Application){
            context.imageLoader.diskCache?.clear()
        }

        fun provideShimmerDrawable(context: Context): ShimmerDrawable {
            val shimmer= Shimmer.ColorHighlightBuilder()
                .setHighlightAlpha(0.93f)
                .setBaseAlpha(0.9f)
                .setAutoStart(true)
                .setWidthRatio(1.6f)
                .setBaseColor(ContextCompat.getColor(context,android.R.color.darker_gray))
                .setHighlightColor(ContextCompat.getColor(context,android.R.color.white))
                .setHighlightAlpha(0.7f)
                .build()
            return ShimmerDrawable().apply {
                setShimmer(shimmer)
            }
        }


        fun hasInternetConnection(application:Application): Boolean {
            val connectivityManager= application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                val networkInfo=connectivityManager.activeNetwork ?: return false
                val capabilities=connectivityManager.getNetworkCapabilities(networkInfo)?: return false
                return when{
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
            connectivityManager.activeNetworkInfo?.run{
                return when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
            return false
        }
    }
}
enum class ImageSize{
    THUMB,
    SMALL,
    MED,
    FULL
}
enum class MimeType{
    JPG,
    PNG
}
