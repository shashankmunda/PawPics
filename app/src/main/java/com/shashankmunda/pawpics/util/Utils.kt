package com.shashankmunda.pawpics.util

import android.app.Application
import android.content.Context
import coil.imageLoader
import dagger.hilt.android.qualifiers.ApplicationContext

class Utils{
    companion object {
         const val BASE_URL:String = "https://api.thecatapi.com/v1/"
         const val OFFSET=30
         const val MAX_LIMIT=20
        fun clearImageCache(context: Application){
            context.imageLoader.diskCache?.clear()
        }
    }
}
