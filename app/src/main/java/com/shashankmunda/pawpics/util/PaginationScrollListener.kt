package com.shashankmunda.pawpics.util

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class PaginationScrollListener constructor(var layoutManager: StaggeredGridLayoutManager) : RecyclerView.OnScrollListener(){
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPositions(null)[0]
        if(!isLoading() && !isLastPage()){
            if(visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0){
                loadMoreItems()
            }
        }
    }
    protected abstract fun loadMoreItems()

    protected abstract fun isLastPage(): Boolean

    protected abstract fun isLoading(): Boolean
}