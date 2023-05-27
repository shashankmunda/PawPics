package com.shashankmunda.pawpics.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
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
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.Utils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CatImageFragment: Fragment(R.layout.cat_image_fragment) {
    private lateinit var catImageId: String
    private val catViewModel: HomeFeedViewModel by activityViewModels()
    private var catImageView: AppCompatImageView?=null
    private var catBitmap:Bitmap?=null
    private val displayMetrics: DisplayMetrics by lazy {
        requireContext().resources.displayMetrics
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catImageId=arguments?.getString("image_id")!!
        val binding=CatImageFragmentBinding.bind(view)
        catImageView=binding.catFullImageView
        setUpToolbar(binding)
        catViewModel.currCatStatus.observe(viewLifecycleOwner){ result ->
            updateImageLoadStatus(result, binding)
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
        Thread.sleep(1000)
        catImageView?.layoutParams!!.height= (1.0f*displayMetrics.widthPixels*cat.height!!).toInt()/cat.width!!
        catImageView?.load(cat.url){
            placeholder(Utils.provideShimmerDrawable(requireContext()))
            allowHardware(false)
            bitmapConfig(Bitmap.Config.ARGB_8888)
            allowConversionToBitmap(true)
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.DISABLED)
            listener(
                onError = { _,_ ->
                    hideProgressBar(binding)
                    catImageView?.setImageResource(R.drawable.ic_baseline_broken_image_24)
                },
                onSuccess = { _, result ->
                    hideProgressBar(binding)
                    binding.catImageToolbar.menu.apply {
                        Log.d("TOOLBAR",getItem(0).title.toString()+getItem(1).toString())
                        getItem(0).isEnabled=true
                        getItem(1).isEnabled=true
                    }
                    catBitmap=result.drawable.toBitmap(cat.width,cat.height,Bitmap.Config.ARGB_8888)
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
                    Utils.downloadImage(requireContext(),catImageId)
                true
            }
            R.id.share -> {
                    Utils.shareImage(requireContext(),catBitmap!!,catImageId)
                    true
                }
            else -> false
            }
        }

    private fun updateImageLoadStatus(
        result: Result<Cat>,
        binding: CatImageFragmentBinding
    ) {
        when (result) {
            is Result.Success -> {
                displayCatImage(result.data!!, binding)
            }
            is Result.Error -> {
                hideProgressBar(binding)
                showRetryBtn(binding)
                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            is Result.Loading -> {
                showProgressBar(binding)
            }
        }
    }

    private fun showRetryBtn(binding: CatImageFragmentBinding) {
        binding.retryButton.visibility=View.VISIBLE
    }

    override fun onDestroyView() {
        catImageView=null
        super.onDestroyView()
    }
}