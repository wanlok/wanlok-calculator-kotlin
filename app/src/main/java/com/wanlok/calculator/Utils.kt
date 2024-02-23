package com.wanlok.calculator

import android.content.Context
import android.util.Log
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

    fun plus(x: BigDecimal, y: BigDecimal): BigDecimal {
        return x.plus(y)
    }

    fun plus(x: String, y: String): String {
        return stripTrailingZeros(plus(BigDecimal(x), BigDecimal(y)).toPlainString())
    }

    fun minus(x: BigDecimal, y: BigDecimal): BigDecimal {
        return x.minus(y)
    }

    fun minus(x: String, y: String): String {
        return stripTrailingZeros(minus(BigDecimal(x), BigDecimal(y)).toPlainString())
    }

    fun multiply(x: BigDecimal, y: BigDecimal): BigDecimal {
        return x.multiply(y)
    }

    fun multiply(x: String, y: String): String {
        return stripTrailingZeros(multiply(BigDecimal(x), BigDecimal(y)).toPlainString())
    }

    fun divide(x: BigDecimal, y: BigDecimal): BigDecimal {
        return x.divide(y, 2, RoundingMode.HALF_UP)
    }

    fun divide(x: String, y: String): String {
        return stripTrailingZeros(divide(BigDecimal(x), BigDecimal(y)).toPlainString())
    }

    fun power(x: BigDecimal, power: BigDecimal): BigDecimal {
        return x.pow(power.toInt())
    }

    fun power(x: String, power: String): String {
        return stripTrailingZeros(power(BigDecimal(x), BigDecimal(power)).toPlainString())
    }

    fun squareroot(x: BigDecimal): BigDecimal {
        return BigDecimal(sqrt(x.toDouble()))
    }

    fun squareroot(x: String): String {
        return stripTrailingZeros(squareroot(BigDecimal(x)).toPlainString())
    }

    fun isZero(x: String): Boolean {
        return BigDecimal(x) != BigDecimal.ZERO
    }

    fun eval(str: String): BigDecimal {
        Log.d("ROBERT", "INPUT FORMULA: " + str)

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

            fun parse(): BigDecimal {
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
            fun parseExpression(): BigDecimal {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.code)) {
                        x = plus(x, parseTerm()) // addition
                    } else if (eat('-'.code)) {
                        x = minus(x, parseTerm()) // subtraction
                    } else {
                        return x
                    }
                }
            }

            fun parseTerm(): BigDecimal {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.code)) {
                        x = multiply(x, parseFactor()) // multiplication
                    } else if (eat('/'.code)) {
                        x = divide(x, parseFactor()) // division
                    } else {
                        return x
                    }
                }
            }

            fun parseFactor(): BigDecimal {
                if (eat('+'.code)) {
                    return BigDecimal("+" + parseFactor().toPlainString()) // unary plus
                }
                if (eat('-'.code)) {
                    return BigDecimal("-" + parseFactor().toPlainString()) // unary minus
                }
                var x: BigDecimal
                val startPos = pos
                if (eat('('.code)) { // parentheses
                    x = parseExpression()
                    if (!eat(')'.code)) {
                        throw RuntimeException("Missing ')'")
                    }
                } else if (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) { // numbers
                    while (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) {
                        nextChar()
                    }
                    x = BigDecimal(str.substring(startPos, pos))
                } else if (ch >= 'a'.code && ch <= 'z'.code) { // functions
                    while (ch >= 'a'.code && ch <= 'z'.code) {
                        nextChar()
                    }
                    val func = str.substring(startPos, pos)
                    if (eat('('.code)) {
                        x = parseExpression()
                        if (!eat(')'.code)) throw RuntimeException("Missing ')' after argument to $func")
                    } else {
                        x = parseFactor()
                    }
                    x =
                        if (func == "sqrt") {
                            squareroot(x)
                        }
//                        else if (func == "sin") Math.sin(
//                            Math.toRadians(
//                                x
//                            )
//                        ) else if (func == "cos") Math.cos(
//                            Math.toRadians(x)
//                        ) else if (func == "tan") Math.tan(Math.toRadians(x))
                        else throw RuntimeException(
                            "Unknown function: $func"
                        )
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }
                if (eat('^'.code)) {
                    x = power(x, parseFactor()) // exponentiation
                }
                return x
            }
        }.parse()
    }
}
