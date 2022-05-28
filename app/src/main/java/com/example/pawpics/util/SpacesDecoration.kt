package com.example.pawpics.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesDecoration(space: Int) : RecyclerView.ItemDecoration() {
    private val mSpace by lazy{
        space
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left=mSpace
        outRect.right=mSpace
        outRect.bottom=mSpace
        if(parent.getChildAdapterPosition(view)==0)
            outRect.top=mSpace
    }
}