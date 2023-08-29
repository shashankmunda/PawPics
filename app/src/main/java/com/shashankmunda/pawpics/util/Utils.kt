package com.shashankmunda.pawpics.util

import android.app.Application
import android.app.DownloadManager
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.shashankmunda.pawpics.BuildConfig
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import java.io.File
import java.io.FileOutputStream

object Utils{

        const val BASE_URL="https://api.thedogapi.com/v1/"
        const val OFFSET=30
        const val BATCH_SIZE=10
        const val cacheSize=(5*1024*1024).toLong()

        fun provideShimmerDrawable(context: Context): ShimmerDrawable {
            val shimmer= Shimmer.ColorHighlightBuilder().apply {
                setHighlightAlpha(0.93f)
                setBaseAlpha(0.9f)
                setAutoStart(true)
                setWidthRatio(1.6f)
                setBaseColor(ContextCompat.getColor(context,android.R.color.darker_gray))
                setHighlightColor(ContextCompat.getColor(context,android.R.color.white))
                setHighlightAlpha(0.7f)
            }.build()
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

        fun shareImage(context:Context,catBitmap:Bitmap,catImageId:String) {
            FileUtils.saveBitmapToCache(context, catBitmap,catImageId)
            val contentUri = FileUtils.getCurrentImageUri(catImageId, context)
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                setDataAndType(contentUri,"image/png")
                putExtra(Intent.EXTRA_STREAM, contentUri)
            }
            shareIntent.clipData = ClipData.newRawUri("", contentUri)
            shareIntent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            startActivity(context,Intent.createChooser(shareIntent,null),null)
        }

        fun saveImage(context: Context,catImageId: String) {
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val url = "https://cdn2.thecatapi.com/images/$catImageId.png"
            val request = DownloadManager.Request(Uri.parse(url)).apply {
                addRequestHeader("x-api-key", BuildConfig.CAT_API_KEY)
                setAllowedOverMetered(true)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$catImageId.png")
                setMimeType("image/png")
                setTitle("$catImageId.png")
                setDescription("File Downloaded!")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            }
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
                request.allowScanningByMediaScanner()
            downloadManager.enqueue(request)
        }
    }

object FileUtils{
    fun getCurrentImageUri(catImageId:String, context: Context): Uri? {
        val catDir = context.externalCacheDir
        val imageCacheFile = File(catDir,"$catImageId.png")
        return FileProvider.getUriForFile(
            context,
            "com.shashankmunda.fileprovider",
            imageCacheFile
        )
    }

    private fun getFileFromExternalCache(catImageId:String, context: Context): File {
        val catDir = context.externalCacheDir
        if (!catDir!!.exists()) catDir.mkdirs()
        val targetFile = File(catDir, "$catImageId.png")
        if (!targetFile.exists()) targetFile.createNewFile()
        return targetFile
    }

    private fun writeBitmapToFile(targetFile:File, bitmap: Bitmap){
        val fileOutputStream = FileOutputStream(targetFile)
        fileOutputStream.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        }
    }

    fun saveBitmapToCache(context: Context, bitmap: Bitmap, catImageId:String) {
        try{
            val targetFile = getFileFromExternalCache(catImageId,context)
            writeBitmapToFile(targetFile,bitmap)
        }
        catch (e:Exception){
            e.printStackTrace()
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