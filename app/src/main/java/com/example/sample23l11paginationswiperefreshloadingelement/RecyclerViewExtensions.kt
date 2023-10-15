package com.example.sample23l11paginationswiperefreshloadingelement

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addVerticalSpace(@DimenRes spaceRes: Int = R.dimen.list_vertical_space) {
    val space = context.resources.getDimensionPixelSize(spaceRes)
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            if (position != parent.adapter?.itemCount?.minus(1)) {
                outRect.bottom = space
            }
//            outRect.bottom = space
        }
    })
}

fun RecyclerView.addPaginationListener(
    linearLayoutManager: LinearLayoutManager,
    itemsToLoad: Int,
    onLoadMore: () -> Unit
) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()
            val itemCount = linearLayoutManager.itemCount
            if (itemsToLoad + lastVisiblePosition >= itemCount) {
                onLoadMore()
            }
        }
    })
}