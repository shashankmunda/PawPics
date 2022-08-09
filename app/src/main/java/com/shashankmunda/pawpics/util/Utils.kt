package com.shashankmunda.pawpics.util

import android.app.Application
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader

class Utils{
    companion object {
         const val BASE_URL:String = "https://api.thecatapi.com/v1/"
         const val OFFSET=30
         const val MAX_LIMIT=20
        @OptIn(ExperimentalCoilApi::class)
        fun clearImageCache(context: Application){
            context.imageLoader.diskCache?.clear()
        }
    }
}
