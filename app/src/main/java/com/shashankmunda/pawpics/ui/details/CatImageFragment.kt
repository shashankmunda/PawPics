package com.shashankmunda.pawpics.ui.details

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEach
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.size.Scale.FILL
import com.shashankmunda.pawpics.R
import com.shashankmunda.pawpics.base.BaseFragment
import com.shashankmunda.pawpics.data.Cat
import com.shashankmunda.pawpics.databinding.CatImageFragmentBinding
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatImageFragment: BaseFragment<CatImageFragmentBinding, CatImageViewModel>() {

    private val args: CatImageFragmentArgs by navArgs()
    private lateinit var catBitmap:Bitmap
    private val displayMetrics: DisplayMetrics by lazy {
        requireContext().resources.displayMetrics
    }

    override var sharedViewModel = true

    override fun getViewModelClass(): Class<CatImageViewModel> = CatImageViewModel::class.java

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
        binding.catImgView.load(cat.url, builder = {
            scale(FILL)
            bitmapConfig(Bitmap.Config.ARGB_8888)
            listener(onSuccess = { _,result ->
                binding.catImageToolbar.menu.forEach {
                    it.isEnabled = true
                }
                catBitmap = result.drawable.toBitmap(cat.width, cat.height, ARGB_8888)
            },
                onError = { _,_ ->
                    binding.catImgView.setImageResource(R.drawable.ic_baseline_broken_image_24)
                })
        })
    }

    private fun setUpToolbar() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.catImageToolbar) { v, insets ->
            val insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(
                top = insets.top,
                left = insets.left,
                right = insets.right
            )
            WindowInsetsCompat.CONSUMED
        }
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