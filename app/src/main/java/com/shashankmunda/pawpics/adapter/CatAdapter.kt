package com.shashankmunda.pawpics.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.example.pawpics.R
import com.shashankmunda.pawpics.model.Cat
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import kotlin.math.roundToInt

@ActivityScoped
class CatAdapter @Inject constructor(@ActivityContext context: Context) :
    RecyclerView.Adapter<CatAdapter.CatView>() {
    private var catsList: ArrayList<Cat> = ArrayList()
    private val displayMetrics: DisplayMetrics by lazy {
        context.resources.displayMetrics
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatView {
        return CatView.getInstance(parent, R.layout.cat_image_holder)
    }

    override fun onBindViewHolder(holder: CatView, position: Int) {
        val cat=catsList[position]
        if(cat.height==null || cat.width==null)
            holder.invalidate()
        else
            holder.bind(cat,displayMetrics)
    }

    override fun getItemCount(): Int {
        return catsList.size
    }

    fun updateData(newCats: ArrayList<Cat>) {
        catsList.clear()
        catsList.addAll(newCats)
        notifyDataSetChanged()
    }


    class CatView(view: View) : RecyclerView.ViewHolder(view) {
        private var imageView: ShapeableImageView= view.findViewById(R.id.cat_image_view)
        companion object{
            fun getInstance(parent: ViewGroup,layoutId: Int): CatView {
                val view=LayoutInflater.from(parent.context).inflate(layoutId,parent,false)
                return CatView(view)
            }
        }
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
        }

        fun bind(cat: Cat, displayMetrics: DisplayMetrics){
            imageView.layoutParams.height =
                ((1.0f * (cat.height!!) * (displayMetrics.widthPixels / 2)) / cat.width!!).roundToInt()
            imageView.load(data= cat.url){
                placeholder(shimmerDrawable)
                allowRgb565(true)
                diskCachePolicy(CachePolicy.ENABLED)
                memoryCachePolicy(CachePolicy.DISABLED)
            }
        }
        fun invalidate() {
            imageView.invalidate()
        }
    }
}