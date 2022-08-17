package com.shashankmunda.pawpics.ui.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.request.CachePolicy
import com.example.pawpics.R
import com.example.pawpics.databinding.CatImageFragmentBinding
import com.shashankmunda.pawpics.model.Cat
import com.shashankmunda.pawpics.ui.CatViewModel
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class CatImageFragment: Fragment(R.layout.cat_image_fragment) {
    private lateinit var catImageId: String
    private val catViewModel:CatViewModel by activityViewModels()
    private var catImageView: AppCompatImageView?=null
    private val displayMetrics: DisplayMetrics by lazy {
        requireContext().resources.displayMetrics
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding=CatImageFragmentBinding.bind(view)
        catImageView=binding.catFullImageView
        setUpToolbar(binding)
        catImageId=arguments?.getString("image_id")!!
        catViewModel.currCatStatus.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    displayCatImage(result.data!!,binding)
                }
                is Result.Error -> {
                    hideProgressBar(binding)
                    Toast.makeText(context,result.message,Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    showProgressBar(binding)
                }
            }
        }
        if(catViewModel.isCatSpecsAvailable(catImageId))
        {
            catViewModel.getCatSpecs(catImageId)?.let { displayCatImage(it,binding) }
        }
        else catViewModel.fetchCatSpecs(lifecycleScope,catImageId)
    }

    private fun hideProgressBar(binding: CatImageFragmentBinding) {
        binding.loadingProgressBar.visibility=View.GONE
    }

    private fun showProgressBar(binding: CatImageFragmentBinding) {
        binding.loadingProgressBar.visibility=View.VISIBLE
    }

    private fun displayCatImage(cat: Cat, binding:CatImageFragmentBinding) {
        catImageView?.setImageBitmap(null)
        catImageView?.layoutParams!!.height= (1.0f*displayMetrics.widthPixels*cat.height!!).toInt()/cat.width!!
        catImageView?.load(cat.url){
            placeholder(Utils.provideShimmerDrawable(requireContext()))
            allowHardware(false)
            bitmapConfig(Bitmap.Config.ARGB_8888)
            allowConversionToBitmap(true)
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.ENABLED)
            listener(
                onError = { _,_ ->
                    hideProgressBar(binding)
                },
                onSuccess = { _, result ->
                    hideProgressBar(binding)
                    val bitmap=result.drawable.toBitmap(cat.width,cat.height,Bitmap.Config.ARGB_8888)
                    try{
                        val targetFile = createTempFile()
                        val fileOutputStream=FileOutputStream(targetFile)
                        if(Build.VERSION.SDK_INT>=30)
                        bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS,100,fileOutputStream)
                        else bitmap.compress(Bitmap.CompressFormat.WEBP,100,fileOutputStream)
                        fileOutputStream.close()
                    }
                    catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            )
        }
    }



    private fun setUpToolbar(binding: CatImageFragmentBinding) {
        binding.catImageToolbar.apply {
            inflateMenu(R.menu.cat_menu)
            setOnMenuItemClickListener(catMenuListener)
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private val catMenuListener = Toolbar.OnMenuItemClickListener { item ->
        when(item.itemId){
            R.id.save -> {
                true
            }
            R.id.share -> {
                val contentUri = getCurrentImageUri()
                val shareIntent: Intent =Intent().apply {
                    action=Intent.ACTION_SEND
                     putExtra(Intent.EXTRA_STREAM, contentUri)
                    type="image/webp"
                    flags=Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                startActivity(Intent.createChooser(shareIntent,null))
                true
            }
            R.id.set_wallpaper -> {
                true
            }
            else -> false
        }
    }

    private fun getCurrentImageUri(): Uri? {
        val catDir = requireContext().externalCacheDir
        val newFile = File(catDir, "$catImageId.webp")
        return getUriForFile(requireContext(), "com.shashankmunda.fileprovider", newFile)
    }

    private fun createTempFile(): File {
        val catDir = context?.externalCacheDir
        if (!catDir!!.exists()) catDir.mkdirs()
        val targetFile = File(catDir, "$catImageId.webp")
        if (!targetFile.exists()) targetFile.createNewFile()
        return targetFile
    }

    override fun onDestroyView() {
        Log.d("DESTROYED","yes")
        catImageView=null
        super.onDestroyView()
    }
}