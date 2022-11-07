package com.swipe.mobile.base.recyclerview

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by Andri Dwi Utomo on 23/6/2022.
 */
class DotsIndicatorDecorationLinear constructor(
    normal: Int = 0xFFC5C5C5.toInt(),
    active: Int = 0xFF5B5B5B.toInt()
) : RecyclerView.ItemDecoration() {

    private val density = Resources.getSystem().displayMetrics.density

    private val activeColor = active
    private val normalColor = normal

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val indicatorRadius = 8f * density
    private val indicatorActive = 14f * density
    private val indicatorPadding = 4f * density

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val itemCount = parent.adapter?.itemCount ?: return
        val totalItemWidth = (indicatorRadius + indicatorPadding) * itemCount
        val indicatorStartX = (parent.width - totalItemWidth) / 2
        //val indicatorStartY = (parent.height - (parent.height * 0.24f)) + (24 * density)
        val indicatorStartY = (parent.height) - (12 * density)

        val layoutManager = parent.layoutManager as? LinearLayoutManager
        val activePosition = layoutManager?.findFirstCompletelyVisibleItemPosition() ?: 0

        drawIndicator(c, indicatorStartX, indicatorStartY, itemCount, activePosition)
    }

    private fun drawIndicator(
        canvas: Canvas,
        indicatorStartX: Float,
        indicatorStartY: Float,
        itemCount: Int,
        activePosition: Int
    ) {
        var size = indicatorRadius
        val itemWidth = indicatorRadius + indicatorPadding
        var start = indicatorStartX
        for (index in 0 until itemCount) {
            if (activePosition == index) {
                paint.color = activeColor
                size += indicatorActive
            } else if ((activePosition + 1) == index) {
                paint.color = normalColor
                size = indicatorRadius
                start = start + indicatorActive
            } else {
                paint.color = normalColor
                size = indicatorRadius
            }

            canvas.drawRoundRect(RectF(start, indicatorStartY, start + size, indicatorStartY + indicatorRadius), indicatorRadius, indicatorRadius, paint)
            start += itemWidth
        }
    }
}