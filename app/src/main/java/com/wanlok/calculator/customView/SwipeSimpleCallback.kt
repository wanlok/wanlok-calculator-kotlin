package com.wanlok.calculator.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.HorizontalScrollView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.wanlok.calculator.NumberCalculatorFragment
import com.wanlok.calculator.R

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

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val view = recyclerView[(viewHolder as NumberCalculatorFragment.Adapter.ViewHolder).adapterPosition]
        val leftHorizontalScrollView: HorizontalScrollView = view.findViewById(R.id.leftHorizontalScrollView)
        val leftCanScrollHorizontally = leftHorizontalScrollView.canScrollHorizontally(-1)
        val rightHorizontalScrollView: HorizontalScrollView = view.findViewById(R.id.rightHorizontalScrollView)
        val rightCanScrollHorizontally = rightHorizontalScrollView.canScrollHorizontally(-1)
        val canScrollHorizontally = leftCanScrollHorizontally || rightCanScrollHorizontally
        return if (canScrollHorizontally) 0 else super.getMovementFlags(recyclerView, viewHolder)
    }
}