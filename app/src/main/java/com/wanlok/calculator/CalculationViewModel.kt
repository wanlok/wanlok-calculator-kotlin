package com.wanlok.calculator

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wanlok.calculator.model.Calculation
import com.wanlok.calculator.model.CalculationLine
import java.math.BigDecimal
import java.util.Date

class CalculationViewModel: ViewModel() {
    private val calculationLines: ArrayList<CalculationLine> = ArrayList()

    val lines = MutableLiveData<ArrayList<CalculationLine>>()

    init {
        calculationLines.add(CalculationLine(0, 0, null, "", "0", true))
    }

    private fun compute(previous: CalculationLine, current: CalculationLine): String {
        val x = BigDecimal(previous.subtotal)
        val y = BigDecimal(current.operand)
        val operator = current.operator
        var operand = ""
        if (operator == "+") {
            operand = x.plus(y).toPlainString()
        } else if (operator == "-") {
            operand = x.minus(y).toPlainString()
        } else if (operator == "*") {
            operand = x.times(y).toPlainString()
        } else if (operator == "/") {
            operand = x.divide(y).toPlainString()
        }
        return operand
    }

    fun text(operand: String) {
        val current = calculationLines[calculationLines.size - 1]
        if (current.operator.equals("=")) {
            return
        }
        current.operand += operand
        lines.postValue(calculationLines)
    }

    fun decimal() {
        val current = calculationLines[calculationLines.size - 1]
        if (current.operator.equals("=")) {
            return
        }
        if (!current.operand.contains(".")) {
            current.operand += "."
        }
        lines.postValue(calculationLines)
    }

    fun backspace() {
        val current = calculationLines[calculationLines.size - 1]
        if (current.operator.equals("=")) {
            return
        }
        val operand = current.operand
        if (operand.isNotEmpty()) {
            current.operand = operand.substring(0, operand.length - 1)
        }
        lines.postValue(calculationLines)
    }

    fun operator(operator: String?) {
        if (operator.equals("=")) {
            equal()
        }
        val current = calculationLines[calculationLines.size - 1]
        if (current.operand.isEmpty()) {
            current.operator = operator
        } else {
            if (calculationLines.size > 1) {
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
        if (current.operator.equals("=")) {
            return
        }
        if (calculationLines.size > 1) {
            val previous = calculationLines[calculationLines.size - 2]
            current.subtotal = compute(previous, current)
        } else {
            current.subtotal = current.operand
        }
        current.last = false
        calculationLines.add(CalculationLine(0, 0, "=", current.subtotal, "0", true))
        lines.postValue(calculationLines)
    }

    fun clear() {
        calculationLines.clear()
        calculationLines.add(CalculationLine(0, 0, null, "", "0", true))
        lines.postValue(calculationLines)
    }
}