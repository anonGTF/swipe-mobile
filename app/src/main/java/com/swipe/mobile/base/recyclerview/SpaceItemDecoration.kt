package com.swipe.mobile.base.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * Created by Andri Dwi Utomo on 17/6/2022.
 */
class SpaceItemDecoration(spaceLeft: Int, spaceTop: Int, spaceRight: Int, spaceBottom: Int) : ItemDecoration() {
    private val spaceLeft: Int
    private val spaceTop: Int
    private val spaceRight: Int
    private val spaceBottom: Int

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.left = spaceLeft
        outRect.right = spaceRight
        outRect.bottom = spaceBottom
        outRect.top = spaceTop
    }

    init {
        this.spaceLeft = spaceLeft
        this.spaceTop = spaceTop
        this.spaceRight = spaceRight
        this.spaceBottom = spaceBottom
    }
}