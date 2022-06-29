package com.example.pawpics.adapter

import android.content.Context
import android.net.Uri
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpics.R
import com.example.pawpics.model.Cat
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import kotlin.math.roundToInt

class CatAdapter @Inject constructor(@ActivityContext context: Context) :
    RecyclerView.Adapter<CatAdapter.CatView>() {
    private var catsList: ArrayList<Cat> = ArrayList()
    private val displayMetrics: DisplayMetrics by lazy {
        context.resources.displayMetrics
    }

    class CatView(view: View) : RecyclerView.ViewHolder(view) {
        var imageview: ImageView = view.findViewById(R.id.cat_image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatView {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cat_image_holder, parent, false)
        return CatView(view)
    }

    override fun onBindViewHolder(holder: CatView, position: Int) {
        holder.imageview.layoutParams.height =
            ((1.0f * (catsList[position].height!!) * (displayMetrics.widthPixels / 2)) / catsList[position].width!!).roundToInt()
        Picasso.get().load(Uri.parse(catsList[position].url)).into(holder.imageview)
    }

    override fun getItemCount(): Int {
        return catsList.size
    }
    fun updateData(newCats: ArrayList<Cat>) {
        catsList.clear()
        catsList.addAll(newCats.distinct())
        notifyDataSetChanged()
    }
}