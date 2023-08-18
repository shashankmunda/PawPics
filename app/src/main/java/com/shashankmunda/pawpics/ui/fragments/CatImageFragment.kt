package com.shashankmunda.pawpics.ui.fragments

import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import com.example.pawpics.R
import com.example.pawpics.databinding.CatImageFragmentBinding
import com.shashankmunda.pawpics.base.BaseFragment
import com.shashankmunda.pawpics.model.Cat
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.Utils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CatImageFragment: BaseFragment<CatImageFragmentBinding,HomeViewModel>() {

    private val args: CatImageFragmentArgs by navArgs()
    private lateinit var catBitmap:Bitmap
    private val displayMetrics: DisplayMetrics by lazy {
        requireContext().resources.displayMetrics
    }
    override var sharedViewModel = true

    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun getViewBinding() = CatImageFragmentBinding.inflate(layoutInflater)

    override fun observeData() {
        mViewModel.currCatStatus.observe(viewLifecycleOwner){ result ->
            updateImageLoadStatus(result, binding)
        }
    }

    override fun initViews() {
        setUpToolbar(binding)
        if(mViewModel.isCatSpecsAvailable(args.imageId))
            mViewModel.getCatSpecs(args.imageId)?.let { displayCatImage(it,binding) }
        else
            mViewModel.fetchCatSpecs(lifecycleScope,args.imageId)
    }


    private fun displayCatImage(cat: Cat, binding:CatImageFragmentBinding) {
        binding.catImgView.layoutParams!!.height= (1.0f*displayMetrics.widthPixels*cat.height!!).toInt()/cat.width!!
        binding.catImgView.load(cat.url){
            placeholder(Utils.provideShimmerDrawable(requireContext()))
            allowHardware(false)
            bitmapConfig(Bitmap.Config.ARGB_8888)
            allowConversionToBitmap(true)
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.DISABLED)
            listener(
                onError = { _,_ ->
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.catImgView.setImageResource(R.drawable.ic_baseline_broken_image_24)
                },
                onSuccess = { _, result ->
                        binding.loadingProgressBar.visibility = View.GONE
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
                    Utils.downloadImage(requireContext(),args.imageId)
                true
            }
            R.id.share -> {
                    Utils.shareImage(requireContext(),catBitmap!!,args.imageId)
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
                binding.loadingProgressBar.visibility = View.GONE
            }
            is Result.Error -> {
                binding.loadingProgressBar.visibility = View.GONE
                showRetryBtn(binding)
                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            is Result.Loading -> {
                binding.loadingProgressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun showRetryBtn(binding: CatImageFragmentBinding) {
        binding.retryButton.visibility=View.VISIBLE
    }
}