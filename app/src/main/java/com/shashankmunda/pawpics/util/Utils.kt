package com.shashankmunda.pawpics.util

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class Utils{
    companion object {
        val BASE_URL by lazy { "https://api.thecatapi.com/v1/" }
        val OFFSET by lazy{30}
        val MAX_LIMIT by lazy{20}
        val cacheSize by lazy{(5*1024*1024).toLong()}
        @OptIn(ExperimentalCoilApi::class)
        fun clearImageCache(context: Application){
            context.imageLoader.diskCache?.clear()
        }

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

         fun getCurrentImageUri(catImageId:String,context: Context): Uri? {
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

        private fun getFileFromExternalStorage(catImageId: String, context: Context):File{
            val catDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            if (!catDir!!.exists()) catDir.mkdirs()
            val targetFile = File(catDir, catImageId)
            if (!targetFile.exists()) targetFile.createNewFile()
            return targetFile
        }
        private fun readBitmapFromFile(tempFile: File): Bitmap {
            val inputStream=FileInputStream(tempFile)
            inputStream.use {
                return BitmapFactory.decodeStream(it)
            }
        }
        private fun writeBitmapToFile(targetFile:File, bitmap: Bitmap){
            val fileOutputStream = FileOutputStream(targetFile)
            fileOutputStream.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            }
        }

        fun saveImageToGallery(catImageId: String,context:Context) {
            val tempFile = getFileFromExternalCache(catImageId,context)
            try {
                val currBitmap = readBitmapFromFile(tempFile)
                if(Build.VERSION.SDK_INT<29) {
                    val targetFile =
                        getFileFromExternalStorage("$catImageId.png", context)
                    writeBitmapToFile(targetFile, currBitmap)
                    Toast.makeText(
                        context,
                        "Image saved: ${targetFile.absolutePath}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    saveToMediaStore(context, catImageId, currBitmap)
                }
            } catch (e: FileSystemException) {
                Toast.makeText(context,"Image not saved :("+e.reason, Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }


        @RequiresApi(Build.VERSION_CODES.Q)
        private fun saveToMediaStore(
            context: Context,
            catImageId: String,
            currBitmap: Bitmap
        ) {
            val resolver = context.contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$catImageId.png")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            contentValues.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS
            )
            val imageUri =
                resolver?.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            val outputStream = resolver?.openOutputStream(Objects.requireNonNull(imageUri!!))
            outputStream.use {
                currBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                Objects.requireNonNull(outputStream)
                Toast.makeText(context, "Image Saved successfully", Toast.LENGTH_SHORT).show()
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
