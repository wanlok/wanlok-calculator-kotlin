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
        return if (x.isEmpty()) "" else BigDecimal(x).stripTrailingZeros().toPlainString()
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

    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0
            fun nextChar() {
                ch = if (++pos < str.length) str[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.code)) x += parseTerm() // addition
                    else if (eat('-'.code)) x -= parseTerm() // subtraction
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.code)) x *= parseFactor() // multiplication
                    else if (eat('/'.code)) x /= parseFactor() // division
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return +parseFactor() // unary plus
                if (eat('-'.code)) return -parseFactor() // unary minus
                var x: Double
                val startPos = pos
                if (eat('('.code)) { // parentheses
                    x = parseExpression()
                    if (!eat(')'.code)) throw RuntimeException("Missing ')'")
                } else if (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) { // numbers
                    while (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) nextChar()
                    x = str.substring(startPos, pos).toDouble()
                } else if (ch >= 'a'.code && ch <= 'z'.code) { // functions
                    while (ch >= 'a'.code && ch <= 'z'.code) nextChar()
                    val func = str.substring(startPos, pos)
                    if (eat('('.code)) {
                        x = parseExpression()
                        if (!eat(')'.code)) throw RuntimeException("Missing ')' after argument to $func")
                    } else {
                        x = parseFactor()
                    }
                    x =
                        if (func == "sqrt") {
                            Math.sqrt(x)
                        } else if (func == "sin") Math.sin(
                            Math.toRadians(
                                x
                            )
                        ) else if (func == "cos") Math.cos(
                            Math.toRadians(x)
                        ) else if (func == "tan") Math.tan(Math.toRadians(x)) else throw RuntimeException(
                            "Unknown function: $func"
                        )
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }
                if (eat('^'.code)) x = Math.pow(x, parseFactor()) // exponentiation
                return x
            }
        }.parse()
    }
}