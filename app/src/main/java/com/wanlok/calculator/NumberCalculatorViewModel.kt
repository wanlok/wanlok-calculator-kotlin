package com.wanlok.calculator

import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.wanlok.calculator.model.ConversionLine

class NumberCalculatorViewModel: ViewModel() {
    private val calculationLines: ArrayList<CalculationLine> = ArrayList()

    val leftSpinnerListLiveData = MutableLiveData<List<BindableSpinnerAdapter.SpinnerItem>>()
    val leftSpinnerSelectedLiveData = MutableLiveData<BindableSpinnerAdapter.SpinnerItem>()
    val leftSpinnerSkippedLiveData = MutableLiveData(false)

    val rightSpinnerListLiveData = MutableLiveData<List<BindableSpinnerAdapter.SpinnerItem>>()
    val rightSpinnerSelectedItem = MutableLiveData<BindableSpinnerAdapter.SpinnerItem>()
    val rightSpinnerSkipped = MutableLiveData(false)
    val rightSpinnerFirstItemSelected = MutableLiveData(false)

    val removing = MutableLiveData(false)
    val clearing = MutableLiveData(false)

    val lines = MutableLiveData<ArrayList<CalculationLine>>()

    init {
        clear()
    }

    var leftFirstSpinnerItem: BindableSpinnerAdapter.SpinnerItem? = null
    var leftSpinnerItems: List<BindableSpinnerAdapter.SpinnerItem>? = null
    var rightFirstSpinnerItem: BindableSpinnerAdapter.SpinnerItem? = null
    var rightSpinnerItems: List<BindableSpinnerAdapter.SpinnerItem>? = null

    private fun combine(firstConversionLine: ConversionLine, conversionLines: List<ConversionLine>?): List<BindableSpinnerAdapter.SpinnerItem> {
        val list = ArrayList<BindableSpinnerAdapter.SpinnerItem>()
        conversionLines?.let { conversionLines ->
            for (conversationLine in listOf(firstConversionLine) + conversionLines.filter { it.selected }) {
                list.add(BindableSpinnerAdapter.SpinnerItem(conversationLine.text, conversationLine))
            }
        }
        return list
    }

    fun setup(leftFirstConversionLine: ConversionLine, rightFirstConversionLine: ConversionLine, conversionLines: List<ConversionLine>?) {
        leftFirstSpinnerItem = BindableSpinnerAdapter.SpinnerItem(leftFirstConversionLine.text, leftFirstConversionLine)
        leftSpinnerItems = combine(leftFirstConversionLine, conversionLines)
        leftSpinnerListLiveData.postValue(leftSpinnerItems)
        leftSpinnerSelectedLiveData.postValue(leftFirstSpinnerItem)
        rightFirstSpinnerItem = BindableSpinnerAdapter.SpinnerItem(rightFirstConversionLine.text, rightFirstConversionLine)
        rightSpinnerItems = combine(rightFirstConversionLine, conversionLines)
        rightSpinnerListLiveData.postValue(rightSpinnerItems)
        rightSpinnerSelectedItem.postValue(rightFirstSpinnerItem)
    }

    private fun getSelectedSpinnerItems(callback: (BindableSpinnerAdapter.SpinnerItem, BindableSpinnerAdapter.SpinnerItem) -> Unit) {
        leftSpinnerSelectedLiveData.value?.let { leftSpinnerItem ->
            rightSpinnerSelectedItem.value?.let { rightSpinnerItem ->
                callback(leftSpinnerItem, rightSpinnerItem)
            }
        }
    }

    private fun isRightSpinnerFirstItemSelected(): Boolean {
        return rightSpinnerSelectedItem.value == rightFirstSpinnerItem
    }

    private fun convert(calculationLine: CalculationLine?) {
        calculationLine?.let { calculationLine ->
            if (calculationLine.operand.isNotEmpty()) {
                getSelectedSpinnerItems { leftSpinnerItem, rightSpinnerItem ->
                    val from = leftSpinnerItem.data as ConversionLine
                    val to = rightSpinnerItem.data as ConversionLine
                    if (from.type == to.type && !isRightSpinnerFirstItemSelected()) {
                        calculationLine.convertedValue = to.decode(from.encode(calculationLine.operand))
                    } else {
                        calculationLine.convertedValue = null
                    }
                }
            }
        }
    }

    private fun resetRightSpinnerIfNeeded() {
        getSelectedSpinnerItems { selectedLeftSpinnerItem, selectedRightSpinnerItem ->
            val from = selectedLeftSpinnerItem.data as ConversionLine
            val to = selectedRightSpinnerItem.data as ConversionLine
            if (from.type != to.type) {
                rightSpinnerFirstItemSelected.value = isRightSpinnerFirstItemSelected()
                rightSpinnerSkipped.value = true
                rightSpinnerSelectedItem.value = rightFirstSpinnerItem
                Handler(Looper.getMainLooper()).postDelayed({
                    rightSpinnerSkipped.value = false
                    rightSpinnerFirstItemSelected.value = false
                }, 100)
            }
        }
    }

    fun leftSpinner() {
        if (leftSpinnerSkippedLiveData.value == true) {
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

    fun remove(index: Int) {
        removing.value = true
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
        Handler(Looper.getMainLooper()).postDelayed({
            removing.value = false
        }, 100)
    }

    fun clear() {
        clearing.value = true
//        leftSpinnerSelectedLiveData.value = leftFirstSpinnerItem
//        rightSpinnerSelectedItem.value = rightFirstSpinnerItem
        calculationLines.clear()
        calculationLines.add(CalculationLine(0, 0, null, "", "0", null, true))
        lines.postValue(calculationLines)
        clearing.value = false
    }

    fun shouldShowErrorMessage(spinnerSkipped: Boolean): Boolean {
        return spinnerSkipped && clearing.value == false && rightSpinnerFirstItemSelected.value == false
    }

    fun shouldScrollToBottom(): Boolean {
        return removing.value == false
    }
}