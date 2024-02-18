package com.wanlok.calculator

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wanlok.calculator.Utils.divide
import com.wanlok.calculator.Utils.isZero
import com.wanlok.calculator.Utils.minus
import com.wanlok.calculator.Utils.multiply
import com.wanlok.calculator.Utils.plus
import com.wanlok.calculator.Utils.stripTrailingZeros
import com.wanlok.calculator.customView.BindableSpinnerAdapter
import com.wanlok.calculator.model.CalculationLine

class NumberCalculatorViewModel: ViewModel() {
    private val leftSpinnerItemList = listOf(
        BindableSpinnerAdapter.SpinnerItem("Numbers", 0, { x: String -> x }, { x: String -> x })
    )

    private val rightSpinnerItemList = listOf(
        BindableSpinnerAdapter.SpinnerItem("Subtotal", 0, { x: String -> x }, { x: String -> x })
    )

    private val spinnerItemList = listOf(
//        BindableSpinnerAdapter.SpinnerItem("Square", 0, { x: String -> squareroot(x) }, { x: String -> power(x, "2") }),
//        BindableSpinnerAdapter.SpinnerItem("Square root", 0, { x: String -> power(x, "2") }, { x: String -> squareroot(x) }),
        BindableSpinnerAdapter.SpinnerItem("cm", 1, { x: String -> x }, { x: String -> x }),
        BindableSpinnerAdapter.SpinnerItem("m", 1, { x: String -> multiply(x, "100") }, { x: String -> divide(x, "100") }),
        BindableSpinnerAdapter.SpinnerItem("ft", 1, { x: String -> multiply(x, "30.48") }, { x: String -> divide(x, "30.48") }),
        BindableSpinnerAdapter.SpinnerItem("m²", 2, { x: String -> x }, { x: String -> x }),
        BindableSpinnerAdapter.SpinnerItem("ft²", 2, { x: String -> multiply(x, "0.09290304") }, { x: String -> divide(x, "0.09290304") }),
    )

    private val calculationLines: ArrayList<CalculationLine> = ArrayList()

    val leftSpinnerItems = MutableLiveData(leftSpinnerItemList + spinnerItemList)
    val leftSpinnerSelectedItem = MutableLiveData<BindableSpinnerAdapter.SpinnerItem>()
    val leftSpinnerSkipped = MutableLiveData(false)

    val rightSpinnerItems = MutableLiveData(rightSpinnerItemList + spinnerItemList)
    val rightSpinnerSelectedItem = MutableLiveData<BindableSpinnerAdapter.SpinnerItem>()
    val rightSpinnerSkipped = MutableLiveData(false)
    val rightSpinnerFirstItemSelected = MutableLiveData(false)

    val clearing = MutableLiveData(false)

    val lines = MutableLiveData<ArrayList<CalculationLine>>()

    init {
        clear()
    }

    private fun getSelectedSpinnerItems(callback: (BindableSpinnerAdapter.SpinnerItem, BindableSpinnerAdapter.SpinnerItem) -> Unit) {
        leftSpinnerSelectedItem.value?.let { leftSpinnerItem ->
            rightSpinnerSelectedItem.value?.let { rightSpinnerItem ->
                callback(leftSpinnerItem, rightSpinnerItem)
            }
        }
    }

    private fun isRightSpinnerFirstItemSelected(): Boolean {
        return rightSpinnerSelectedItem.value == rightSpinnerItemList.first()
    }

    private fun convert(calculationLine: CalculationLine?) {
        calculationLine?.let { calculationLine ->
            if (calculationLine.operand.isNotEmpty()) {
                getSelectedSpinnerItems { leftSpinnerItem, rightSpinnerItem ->
                    if (leftSpinnerItem.type == rightSpinnerItem.type && !isRightSpinnerFirstItemSelected()) {
                        calculationLine.convertedValue = rightSpinnerItem.decode(leftSpinnerItem.encode(calculationLine.operand))
                    } else {
                        calculationLine.convertedValue = null
                    }
                }
            }
        }
    }

    private fun resetRightSpinnerIfNeeded() {
        getSelectedSpinnerItems { selectedLeftSpinnerItem, selectedRightSpinnerItem ->
            if (selectedLeftSpinnerItem.type != selectedRightSpinnerItem.type) {
                rightSpinnerFirstItemSelected.value = isRightSpinnerFirstItemSelected()
                rightSpinnerSkipped.value = true
                rightSpinnerSelectedItem.value = rightSpinnerItemList.first()
                Handler(Looper.getMainLooper()).postDelayed({
                    rightSpinnerSkipped.value = false
                    rightSpinnerFirstItemSelected.value = false
                }, 100)
            }
        }
    }

    fun leftSpinner() {
        if (leftSpinnerSkipped.value == true) {
            return
        }
        resetRightSpinnerIfNeeded()
        for (calculationLine in calculationLines) {
            convert(calculationLine)
        }
        lines.postValue(calculationLines)
    }

    fun rightSpinner() {
        if (rightSpinnerSkipped.value == true) {
            return
        }
        resetRightSpinnerIfNeeded()
        for (calculationLine in calculationLines) {
            convert(calculationLine)
        }
        lines.postValue(calculationLines)
    }

    private fun compute(previous: CalculationLine, current: CalculationLine): String {
        val x = previous.subtotal
        val y = current.operand
        val operator = current.operator
        var operand = previous.subtotal
        if (operator == "+") {
            operand = plus(x, y)
        } else if (operator == "-") {
            operand = minus(x, y)
        } else if (operator == "×") {
            operand = multiply(x, y)
        } else if (operator == "÷" && isZero(y)) {
            operand = divide(x, y)
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
        current.operand = stripTrailingZeros(current.operand)
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
            calculationLines.add(CalculationLine(0, 0, operator, "", "0", null, true))
        }
        convert(current)
        lines.postValue(calculationLines)
    }

    fun equal() {
        var current = calculationLines[calculationLines.size - 1]
        var next: CalculationLine? = null
        if ((current.operator == null && current.operand.isEmpty()) || current.operand == ".") {
            return
        }
        current.operand = stripTrailingZeros(current.operand)
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
            next = CalculationLine(0, 0, "=", current.subtotal, current.subtotal, null, true)
            calculationLines.add(next)
        }
        convert(current)
        convert(next)
        lines.postValue(calculationLines)
    }

    fun clear() {
        clearing.value = true
        leftSpinnerSelectedItem.value = leftSpinnerItemList.first()
        rightSpinnerSelectedItem.value = rightSpinnerItemList.first()
        calculationLines.clear()
        calculationLines.add(CalculationLine(0, 0, null, "", "0", null, true))
        lines.postValue(calculationLines)
        clearing.value = false
    }

    fun remove(index: Int) {
        calculationLines.removeAt(index)
        if (calculationLines.size == 0) {
            calculationLines.add(CalculationLine(0, 0, null, "", "0", null, true))
        }
        var current: CalculationLine? = null
        for (i in 0 until calculationLines.size) {
            current = calculationLines[i]
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
        convert(current)
        lines.postValue(calculationLines)
    }

    fun shouldShowErrorMessage(spinnerSkipped: Boolean): Boolean {
        return spinnerSkipped && clearing.value == false && rightSpinnerFirstItemSelected.value == false
    }
}