package com.app.khavdawala.apputils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerOnScrollListener(
    private val layoutManager: RecyclerView.LayoutManager,
    private val visibleThreshold: Int
) : RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var loading = true
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            onLoadMore()
            loading = true
        }
    }

    abstract fun onLoadMore()
}