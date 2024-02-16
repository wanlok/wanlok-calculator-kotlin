package com.wanlok.calculator

import android.content.Context
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP

object Utils {
    fun dp(dp: Int, context: Context?): Int {
        var value = 0F
        if (context != null) {
            value = TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
        }
        return value.toInt()
    }
}