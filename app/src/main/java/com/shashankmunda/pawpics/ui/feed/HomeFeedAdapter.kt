package com.shashankmunda.pawpics.ui.feed

import android.content.Context
import android.graphics.Bitmap.Config
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import coil.ImageLoader
import coil.request.ImageRequest
import com.shashankmunda.pawpics.R
import com.shashankmunda.pawpics.base.BaseAdapter
import com.shashankmunda.pawpics.data.Cat
import com.shashankmunda.pawpics.databinding.CatImageHolderBinding
import com.shashankmunda.pawpics.util.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
import kotlin.math.roundToInt

@FragmentScoped
class HomeFeedAdapter @Inject constructor(@ApplicationContext var context: Context,var imageLoader: ImageLoader,var imageRequest: ImageRequest.Builder) : BaseAdapter<Cat, CatImageHolderBinding>() {
    private val displayMetrics: DisplayMetrics by lazy {
        context.resources.displayMetrics
    }
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup) = CatImageHolderBinding.inflate(inflater,parent,false)

    override fun bindItem(binding: CatImageHolderBinding, item: Cat) {
        binding.catImageView.layoutParams.height =
            ((1.0f * (item.height!!) * (displayMetrics.widthPixels / 2)) / item.width!!).roundToInt()
        val request = imageRequest
            .data(item.url)
            .bitmapConfig(Config.ALPHA_8)
            .target(binding.catImageView)
            .placeholder(Utils.provideShimmerDrawable())
            .listener(
                onError ={_,_ ->
                    binding.catImageView.setImageResource(R.drawable.ic_baseline_broken_image_24)
                }
            )
            .build()
        disposables.add(imageLoader.enqueue(request))
        binding.catImageView.setOnClickListener {
            val action= HomeFeedFragmentDirections.actionHomeFragmentToFullCatImageFragment(item.id)
            it.findNavController().navigate(action)
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        disposables.forEach {
            it.dispose()
        }
        super.onViewRecycled(holder)
    }

    fun isEmpty() = items.isEmpty()
}