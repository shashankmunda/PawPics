package com.shashankmunda.pawpics.ui.feed

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import coil3.load
import coil3.request.ImageResult
import com.shashankmunda.pawpics.base.BaseAdapter
import com.shashankmunda.pawpics.data.Cat
import com.shashankmunda.pawpics.databinding.CatImageHolderBinding
import com.shashankmunda.pawpics.util.getExtensionFromUrl
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
import kotlin.math.roundToInt

@FragmentScoped
class HomeFeedAdapter @Inject constructor(
    @ApplicationContext var context: Context
) : BaseAdapter<Pair<Cat, ImageResult>, CatImageHolderBinding>() {
    private val displayMetrics: DisplayMetrics by lazy {
        context.resources.displayMetrics
    }
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup) = CatImageHolderBinding.inflate(inflater,parent,false)

    override fun bindItem(binding: CatImageHolderBinding, item: Pair<Cat,ImageResult>) {
        binding.catImageView.layoutParams.height =
            ((1.0f * (item.first.height) * (displayMetrics.widthPixels / 2)) / item.first.width).roundToInt()
        binding.catImageView.load(item.first.url)
        binding.catImageView.setOnClickListener {
            val action= HomeFeedFragmentDirections.actionHomeFragmentToFullCatImageFragment(item.first.id, getExtensionFromUrl(item.first.url)!!)
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

    fun clearItems() = items.clear()
}