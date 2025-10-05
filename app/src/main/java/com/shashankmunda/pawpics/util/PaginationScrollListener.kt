package com.shashankmunda.pawpics.util

import android.widget.AbsListView.OnScrollListener
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class PaginationScrollListener(private var layoutManager: StaggeredGridLayoutManager) : RecyclerView.OnScrollListener(){
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPositions(null)[0];
        if(!isLoading() && totalItemCount <= (lastVisibleItemPosition + 2)){
            loadMoreItems()
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if(newState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
            isScrolling = true
        }
    }
    protected abstract fun loadMoreItems()

    protected abstract fun isLastPage(): Boolean

    protected abstract fun isLoading(): Boolean

    protected var isScrolling = false
}