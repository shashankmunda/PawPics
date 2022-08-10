package com.shashankmunda.pawpics.adapter

import android.content.Context
import android.graphics.Bitmap
import android.text.Layout
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.decode.Decoder
import coil.dispose
import coil.load
import coil.request.CachePolicy
import coil.size.Scale
import com.example.pawpics.NavGraphDirections
import com.example.pawpics.R
import com.example.pawpics.databinding.CatImageHolderBinding
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.imageview.ShapeableImageView
import com.shashankmunda.pawpics.model.Cat
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@FragmentScoped
class CatAdapter @Inject constructor(@ApplicationContext context: Context) :
    RecyclerView.Adapter<CatAdapter.CatViewHolder>() {
    private var catsList: ArrayList<Cat> = ArrayList()
    private val displayMetrics: DisplayMetrics by lazy {
        context.resources.displayMetrics
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val binding=CatImageHolderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val cat=catsList[position]
        if(cat.height==null || cat.width==null)
            holder.invalidate()
        else
            holder.bind(cat,displayMetrics)
    }

    override fun onViewRecycled(holder: CatViewHolder) {
        holder.dispose()
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return catsList.size
    }

    fun updateData(newCats: ArrayList<Cat>) {
        catsList.clear()
        catsList.addAll(newCats)
        notifyDataSetChanged()
    }


    class CatViewHolder(binding: CatImageHolderBinding) : RecyclerView.ViewHolder(binding.root) {
        private var imageView: ShapeableImageView= binding.catImageView
        private val shimmerDrawable:ShimmerDrawable
        init{
            val shimmer: Shimmer =Shimmer.ColorHighlightBuilder()
                .setHighlightAlpha(0.93f)
                .setBaseAlpha(0.9f)
                .setAutoStart(true)
                .setWidthRatio(1.6f)
                .setBaseColor(ContextCompat.getColor(this.itemView.context,android.R.color.darker_gray))
                .setHighlightColor(ContextCompat.getColor(this.itemView.context,android.R.color.white))
                .setHighlightAlpha(0.7f)
                .build()
            shimmerDrawable=ShimmerDrawable().apply {
                setShimmer(shimmer)
            }
            binding.root.setOnClickListener {
                val action=NavGraphDirections.actionGlobalHomeFragment()
                it.findNavController().navigate(action)
            }
        }

        fun bind(cat: Cat, displayMetrics: DisplayMetrics){
            imageView.layoutParams.height =
                ((1.0f * (cat.height!!) * (displayMetrics.widthPixels / 2)) / cat.width!!).roundToInt()
            imageView.load(data= cat.url){
                placeholder(shimmerDrawable)
                allowHardware(false)
                memoryCachePolicy(CachePolicy.DISABLED)
                diskCachePolicy(CachePolicy.ENABLED)
                bitmapConfig(Bitmap.Config.RGB_565)
                allowRgb565(true)
            }
        }
        fun invalidate() {
            imageView.invalidate()
        }
        fun dispose(){
            imageView.dispose()
        }
    }
}