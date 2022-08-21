package com.shashankmunda.pawpics.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.dispose
import coil.load
import coil.request.CachePolicy
import coil.size.Precision
import com.example.pawpics.databinding.CatImageHolderBinding
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.imageview.ShapeableImageView
import com.shashankmunda.pawpics.model.Cat
import com.shashankmunda.pawpics.ui.fragments.HomeFragmentDirections
import com.shashankmunda.pawpics.util.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
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
        private val shimmerDrawable:ShimmerDrawable = Utils.provideShimmerDrawable(binding.root.context)

        fun bind(cat: Cat, displayMetrics: DisplayMetrics){
            imageView.layoutParams.height =
                ((1.0f * (cat.height!!) * (displayMetrics.widthPixels / 2)) / cat.width!!).roundToInt()
            imageView.load(data= cat.url){
                placeholder(shimmerDrawable)
                allowHardware(false)
                precision(Precision.INEXACT)
                memoryCachePolicy(CachePolicy.DISABLED)
                allowRgb565(true)
            }
            imageView.setOnClickListener {
                val action=HomeFragmentDirections.actionHomeFragmentToFullCatImageFragment(cat.id)
                it.findNavController().navigate(action)
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