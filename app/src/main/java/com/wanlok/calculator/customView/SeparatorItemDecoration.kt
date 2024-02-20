package com.wanlok.calculator.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wanlok.calculator.ConversionFragment
import com.wanlok.calculator.R
import com.wanlok.calculator.Utils

class SeparatorItemDecoration(var context: Context, private val separatorHeight: Int): RecyclerView.ItemDecoration() {
    private val separatorPaint: Paint = Paint()

    init {
        separatorPaint.color = ContextCompat.getColor(context, R.color.gray_2)
        separatorPaint.strokeWidth = separatorHeight.toFloat()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = separatorHeight
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            val viewHolder = parent.findViewHolderForAdapterPosition(position)
            val nextViewHolder = parent.findViewHolderForAdapterPosition(position + 1)
            if (viewHolder !is ConversionFragment.ConversionRecyclerViewAdapter.HeaderViewHolder &&
                nextViewHolder !is ConversionFragment.ConversionRecyclerViewAdapter.HeaderViewHolder) {
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom = top + separatorHeight
                c.drawLine(left.toFloat() + Utils.dp(48, context), top.toFloat(), right.toFloat(), bottom.toFloat(), separatorPaint)
            }
        }
    }
}