package com.wanlok.calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wanlok.calculator.model.CalculationLine
import java.math.BigDecimal
import java.math.RoundingMode

class NumberCalculatorViewModel: ViewModel() {
    private val calculationLines: ArrayList<CalculationLine> = ArrayList()

    val lines = MutableLiveData<ArrayList<CalculationLine>>()

    init {
        clear()
    }

    private fun compute(previous: CalculationLine, current: CalculationLine): String {
        val x = BigDecimal(previous.subtotal)
        val y = BigDecimal(current.operand)
        val operator = current.operator
        var operand = previous.subtotal
        if (operator == "+") {
            operand = x.plus(y).toPlainString()
        } else if (operator == "-") {
            operand = x.minus(y).toPlainString()
        } else if (operator == "×") {
            operand = x.times(y).toPlainString()
        } else if (operator == "÷" && y != BigDecimal.ZERO) {
            operand = x.divide(y, 2, RoundingMode.HALF_UP).toPlainString()
        }
        return operand
    }

    fun text(operand: String) {
        val current = calculationLines[calculationLines.size - 1]
        if (current.operator == "=") {
            return
        }
        current.operand += operand
        lines.postValue(calculationLines)
    }

    fun decimal() {
        val current = calculationLines[calculationLines.size - 1]
        if (current.operator == "=") {
            return
        }
        if (!current.operand.contains(".")) {
            current.operand += "."
        }
        lines.postValue(calculationLines)
    }

    fun backspace() {
        val current = calculationLines[calculationLines.size - 1]
        if (current.operator == "=") {
            return
        }
        val operand = current.operand
        if (operand.isNotEmpty()) {
            current.operand = operand.substring(0, operand.length - 1)
        }
        lines.postValue(calculationLines)
    }

    fun operator(operator: String) {
        val current = calculationLines[calculationLines.size - 1]
        if ((current.operator == null && current.operand.isEmpty()) || current.operand == ".") {
            return
        }
        if (current.operand.isEmpty()) {
            current.operator = operator
        } else {
            if (calculationLines.size > 1 && current.operator != "=") {
                val previous = calculationLines[calculationLines.size - 2]
                current.subtotal = compute(previous, current)
            } else {
                current.subtotal = current.operand
            }
            current.last = false
            calculationLines.add(CalculationLine(0, 0, operator, "", "0", true))
        }
        lines.postValue(calculationLines)
    }

    fun equal() {
        val current = calculationLines[calculationLines.size - 1]
        if ((current.operator == null && current.operand.isEmpty()) || current.operand == ".") {
            return
        }
        if (current.operand.isEmpty()) {
            if (calculationLines.size > 1) {
                val previous = calculationLines[calculationLines.size - 2]
                current.operator = "="
                current.operand = previous.subtotal
                current.subtotal = previous.subtotal
            }
        } else {
            if (calculationLines.size > 1) {
                val previous = calculationLines[calculationLines.size - 2]
                current.subtotal = compute(previous, current)
            } else {
                current.subtotal = current.operand
            }
            current.last = false
            calculationLines.add(CalculationLine(0, 0, "=", current.subtotal, current.subtotal, true))
        }
        lines.postValue(calculationLines)
    }

    fun clear() {
        calculationLines.clear()
        calculationLines.add(CalculationLine(0, 0, null, "", "0", true))
        lines.postValue(calculationLines)
    }

    fun remove(index: Int) {
        calculationLines.removeAt(index)
        if (calculationLines.size == 0) {
            calculationLines.add(CalculationLine(0, 0, null, "", "0", true))
        }
        for (i in 0 until calculationLines.size) {
            val current = calculationLines[i]
            if (i == 0) {
                current.subtotal = current.operand
                current.operator = null
            } else if (current.operand.isNotEmpty()) {
                val previous = calculationLines[i - 1]
                current.subtotal = compute(previous, current)
                if (current.operator == "=") {
                    current.operand = current.subtotal
                }
            }
            current.last = i == calculationLines.size - 1
        }
//        var i = 0
//        while (i < calculationLines.size) {
//            if (i > 0) {
//                val previous = calculationLines[i - 1]
//                val current = calculationLines[i]
//                if (previous.operator == "=" && current.operator == "=") {
//                    calculationLines.removeAt(i - 1)
//                    i = 0
//                }
//            }
//            i += 1
//        }
        lines.postValue(calculationLines)
    }
}