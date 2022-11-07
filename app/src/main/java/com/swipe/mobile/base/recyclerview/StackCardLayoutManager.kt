package com.swipe.mobile.base.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.swipe.mobile.R

class StackCardLayoutManager(
    private val maxItemCount: Int
) : RecyclerView.LayoutManager() {

    private var itemCount: Int? = 0

    private val addedChildren: List<View>
        get() = (0 until childCount).map { getChildAt(it) ?: throw NullPointerException() }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT)

    override fun isAutoMeasureEnabled(): Boolean = true

    override fun onLayoutChildren(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ) {
        itemCount = state.itemCount
        if (itemCount == 0) {
            return
        }
        if (itemCount!! > maxItemCount) {
            itemCount = maxItemCount
            //throw RuntimeException("Can not set more Item than maxItemCount")
        }

        detachAndScrapAttachedViews(recycler)

        for (i in 0 until itemCount!!) {
            val view = recycler.getViewForPosition(i)
            measureChild(view, 0, 0)
            addView(view)
            val layoutParams = view.layoutParams as RecyclerView.LayoutParams
            var left = (view.measuredWidth * i * 0.40).toInt()
            if (itemCount!! == 2) {
                left = (view.measuredWidth * i * 0.90).toInt()
            } else if (itemCount!! == 3) {
                left = (view.measuredWidth * i * 0.60).toInt()
            }
            val top = layoutParams.marginStart
            val right = left + view.measuredWidth
            val bottom = view.measuredHeight + layoutParams.marginEnd
            layoutDecorated(view, left, top, right, bottom)
            view.setTag(InitializedPosition.RIGHT.key, top)
        }
    }


    private enum class InitializedPosition(val key: Int) {
        RIGHT(R.integer.right)
    }
}