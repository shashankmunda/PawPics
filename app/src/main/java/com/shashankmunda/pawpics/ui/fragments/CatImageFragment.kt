package com.shashankmunda.pawpics.ui.fragments

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.forEach
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.request.ImageRequest
import coil.size.Scale
import com.shashankmunda.pawpics.R
import com.shashankmunda.pawpics.base.BaseFragment
import com.shashankmunda.pawpics.databinding.CatImageFragmentBinding
import com.shashankmunda.pawpics.model.Cat
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CatImageFragment: BaseFragment<CatImageFragmentBinding,HomeViewModel>() {
    @Inject lateinit var imageLoader: ImageLoader

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
            when (result) {
                is Result.Success -> {
                    displayCatImage(result.data!!)
                    binding.loadingProgressBar.visibility = View.GONE
                }
                is Result.Error -> {
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.retryButton.visibility = View.VISIBLE
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.loadingProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun initViews() {
        setUpToolbar()
        mViewModel.fetchCatSpecs(args.imageId)
    }


    private fun displayCatImage(cat: Cat) {
        binding.catImgView.layoutParams!!.height= (1.0f*displayMetrics.widthPixels*cat.height!!).toInt()/cat.width!!
        val request = ImageRequest.Builder(requireContext())
            .data(cat.url)
            .scale(Scale.FILL)
            .bitmapConfig(Bitmap.Config.ARGB_8888)
            .target(binding.catImgView)
            .listener(
                onError = { _,_ ->
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.catImgView.setImageResource(R.drawable.ic_baseline_broken_image_24)
                },
                onSuccess = { _, result ->
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.catImageToolbar.menu.forEach {
                        it.isEnabled = true
                    }
                    catBitmap = result.drawable.toBitmap(cat.width,cat.height,ARGB_8888)
                }
            )
            .build()
        imageLoader.enqueue(request)
    }

    private fun setUpToolbar() {
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
                Utils.saveImage(requireContext(),args.imageId)
                true
            }
            R.id.share -> {
                    Utils.shareImage(requireContext(), catBitmap,args.imageId)
                    true
                }
            R.id.set_wallpaper ->{
                WallpaperManager.getInstance(requireContext()).setBitmap(catBitmap)
                true
            }
            else -> false
            }
        }
}