package com.wanlok.calculator.customView

import androidx.recyclerview.widget.RecyclerView

interface SwipeListener {
    fun isSwipeAllowed(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Boolean
    fun onSwipe(direction: Int, position: Int)
}