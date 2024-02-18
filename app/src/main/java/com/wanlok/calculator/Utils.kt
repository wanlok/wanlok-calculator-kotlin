package com.wanlok.calculator

import android.content.Context
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.sqrt

object Utils {
    fun dp(dp: Int, context: Context?): Int {
        var value = 0F
        if (context != null) {
            value = TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
        }
        return value.toInt()
    }

    fun stripTrailingZeros(x: String): String {
        return BigDecimal(x).stripTrailingZeros().toPlainString()
    }

    fun plus(x: String, y: String): String {
        return stripTrailingZeros(BigDecimal(x).plus(BigDecimal(y)).toPlainString())
    }

    fun minus(x: String, y: String): String {
        return stripTrailingZeros(BigDecimal(x).minus(BigDecimal(y)).toPlainString())
    }

    fun multiply(x: String, y: String): String {
        return stripTrailingZeros(BigDecimal(x).multiply(BigDecimal(y)).toPlainString())
    }

    fun divide(x: String, y: String): String {
        return stripTrailingZeros(BigDecimal(x).divide(BigDecimal(y), 2, RoundingMode.HALF_UP).toPlainString())
    }

    fun power(x: String, power: String): String {
        return stripTrailingZeros(BigDecimal(x).pow(power.toInt()).toPlainString())
    }

    fun squareroot(x: String): String {
        return stripTrailingZeros(sqrt(x.toDouble()).toString())
    }

    fun isZero(x: String): Boolean {
        return BigDecimal(x) != BigDecimal.ZERO
    }
}