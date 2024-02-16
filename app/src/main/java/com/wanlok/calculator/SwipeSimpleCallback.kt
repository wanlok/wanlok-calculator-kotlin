package com.wanlok.calculator

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class SwipeSimpleCallback(private var context: Context, var swipeListener: SwipeListener): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val itemView = viewHolder.itemView
        if (dX > 0) {
            val paint = Paint()
            paint.color = ContextCompat.getColor(context, R.color.white)
            c.drawRect(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat(), paint)

//            val customView: View = createCustomView(context)
//            val customViewWidth = customView.measuredWidth
//            val customViewHeight = customView.measuredHeight
//            val left = itemView.left + dX.toInt() - customViewWidth
//            val top = itemView.top + (itemView.height - customViewHeight) / 2
//            c.save()
//            c.translate(left.toFloat(), top.toFloat())
//            customView.draw(c)
//            c.restore()
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

//    private fun createCustomView(context: Context): View {
//        // Create your custom view (e.g., an icon or any other view)
//        // For simplicity, creating a simple colored rectangle
//        val customView = View(context)
//        customView.setBackgroundColor(Color.RED)
//        customView.measure(
//            View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY),
//            View.MeasureSpec.makeMeasureSpec(50, View.MeasureSpec.EXACTLY)
//        )
//        customView.layout(0, 0, customView.measuredWidth, customView.measuredHeight)
//        return customView
//    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipeListener.onSwipe(direction, viewHolder.adapterPosition)
    }
}