package com.shashankmunda.pawpics.ui.fragments

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import coil.dispose
import coil.load
import coil.request.CachePolicy.ENABLED
import coil.size.Precision
import com.example.pawpics.R
import com.example.pawpics.databinding.CatImageHolderBinding
import com.shashankmunda.pawpics.base.BaseAdapter
import com.shashankmunda.pawpics.model.Cat
import com.shashankmunda.pawpics.util.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
import kotlin.math.roundToInt

@FragmentScoped
class HomeFeedAdapter @Inject constructor(@ApplicationContext context: Context) :
    BaseAdapter<Cat, CatImageHolderBinding>() {
    private val displayMetrics: DisplayMetrics by lazy {
        context.resources.displayMetrics
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup) = CatImageHolderBinding.inflate(inflater,parent,false)

    override fun bindItem(binding: CatImageHolderBinding, item: Cat) {
        binding.catImageView.layoutParams.height =
            ((1.0f * (item.height!!) * (displayMetrics.widthPixels / 2)) / item.width!!).roundToInt()
        binding.catImageView.load(data= item.url){
            placeholder(Utils.provideShimmerDrawable(binding.root.context))
            allowHardware(false)
            precision(Precision.INEXACT)
            diskCachePolicy(ENABLED)
            memoryCachePolicy(ENABLED)
            allowRgb565(true)
            listener(
                onError ={_,_ ->
                    binding.catImageView.setImageResource(R.drawable.ic_baseline_broken_image_24)
                }
            )
        }
        binding.catImageView.setOnClickListener {
            val action=HomeFeedFragmentDirections.actionHomeFragmentToFullCatImageFragment(item.id)
            it.findNavController().navigate(action)
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.binding.catImageView.dispose()
        super.onViewRecycled(holder)
    }

    fun updateData(newCats: ArrayList<Cat>) = setItems(newCats)
}