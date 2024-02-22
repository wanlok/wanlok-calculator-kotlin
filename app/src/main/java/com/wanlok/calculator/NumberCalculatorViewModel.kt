package com.wanlok.calculator

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wanlok.calculator.Utils.divide
import com.wanlok.calculator.Utils.eval
import com.wanlok.calculator.Utils.isZero
import com.wanlok.calculator.Utils.minus
import com.wanlok.calculator.Utils.multiply
import com.wanlok.calculator.Utils.plus
import com.wanlok.calculator.Utils.stripTrailingZeros
import com.wanlok.calculator.customView.BindableSpinnerAdapter
import com.wanlok.calculator.model.CalculationLine
import com.wanlok.calculator.model.ConversionLine

class NumberCalculatorViewModel: ViewModel() {
    var leftSpinnerItemList: List<BindableSpinnerAdapter.SpinnerItem> = emptyList()
    val leftSpinnerItemListLiveData = MutableLiveData<List<BindableSpinnerAdapter.SpinnerItem>>()
    val leftSpinnerSelectedItemLiveData = MutableLiveData<BindableSpinnerAdapter.SpinnerItem>()
    val leftSpinnerSkippedLiveData = MutableLiveData(false)

    var rightSpinnerItemList: List<BindableSpinnerAdapter.SpinnerItem> = emptyList()
    val rightSpinnerItemListLiveData = MutableLiveData<List<BindableSpinnerAdapter.SpinnerItem>>()
    val rightSpinnerSelectedItemLiveData = MutableLiveData<BindableSpinnerAdapter.SpinnerItem>()
    val rightSpinnerSkippedLiveData = MutableLiveData(false)

    val removing = MutableLiveData(false)

    val calculationLineList: ArrayList<CalculationLine> = ArrayList()
    val calculationLineListLiveData = MutableLiveData<ArrayList<CalculationLine>>()

    init {
        clear()
    }

    private fun combine(firstConversionLine: ConversionLine, conversionLines: List<ConversionLine>?): List<BindableSpinnerAdapter.SpinnerItem> {
        val list = ArrayList<BindableSpinnerAdapter.SpinnerItem>()
        conversionLines?.let { conversionLines ->
            for (conversationLine in listOf(firstConversionLine) + conversionLines.filter { it.selected }.sortedBy { it.conversionOrder }) {
                list.add(BindableSpinnerAdapter.SpinnerItem(conversationLine.text, conversationLine))
            }
        }
        return list
    }

    fun setup(leftFirstConversionLine: ConversionLine, rightFirstConversionLine: ConversionLine, conversionLines: List<ConversionLine>?) {
        leftSpinnerItemList = combine(leftFirstConversionLine, conversionLines)
        leftSpinnerItemListLiveData.postValue(leftSpinnerItemList)
        rightSpinnerItemList = combine(rightFirstConversionLine, conversionLines)
        rightSpinnerItemListLiveData.postValue(rightSpinnerItemList)
    }

    private fun getSpinnerSelectedItems(callback: (BindableSpinnerAdapter.SpinnerItem, BindableSpinnerAdapter.SpinnerItem) -> Unit) {
        leftSpinnerSelectedItemLiveData.value?.let { leftSpinnerSelectedItem ->
            rightSpinnerSelectedItemLiveData.value?.let { rightSpinnerSelectedItem ->
                callback(leftSpinnerSelectedItem, rightSpinnerSelectedItem)
            }
        }
    }

    private fun isRightSpinnerFirstItemSelected(): Boolean {
        return rightSpinnerSelectedItemLiveData.value == rightSpinnerItemListLiveData.value?.first()
    }

    val PLACEHOLDER = "x"

    private fun convert(calculationLine: CalculationLine?) {
        calculationLine?.let { calculationLine ->
            if (calculationLine.operand.isNotEmpty()) {
                getSpinnerSelectedItems { leftSpinnerSelectedItem, rightSpinnerSelectedItem ->
                    val leftConversionLine = leftSpinnerSelectedItem.data as ConversionLine
                    val rightConversionLine = rightSpinnerSelectedItem.data as ConversionLine
                    if (leftConversionLine.conversionId == rightConversionLine.conversionId && !isRightSpinnerFirstItemSelected()) {
                        var convertedValue = eval(leftConversionLine.encode.replace(PLACEHOLDER, calculationLine.operand)).toString()
                        convertedValue = eval(rightConversionLine.decode.replace(PLACEHOLDER, convertedValue)).toString()
                        calculationLine.convertedValue = stripTrailingZeros(convertedValue)
                    } else {
                        calculationLine.convertedValue = null
                    }
                }
            }
        }
    }

    private fun resetRightSpinnerIfNeeded() {
        getSpinnerSelectedItems { leftSpinnerSelectedItem, rightSpinnerSelectedItem ->
            val from = leftSpinnerSelectedItem.data as ConversionLine
            val to = rightSpinnerSelectedItem.data as ConversionLine
            if (from.conversionId != to.conversionId) {
                rightSpinnerSkippedLiveData.postValue(true)
                rightSpinnerSelectedItemLiveData.postValue(rightSpinnerItemListLiveData.value?.first())
                Handler(Looper.getMainLooper()).postDelayed({
                    rightSpinnerSkippedLiveData.postValue(false)
                }, 100)
            }
        }
    }

    fun leftSpinner() {
        if (leftSpinnerSkippedLiveData.value == true) {
            return
        }
        resetRightSpinnerIfNeeded()
        for (calculationLine in calculationLineList) {
            convert(calculationLine)
        }
        calculationLineListLiveData.postValue(calculationLineList)
    }

    fun rightSpinner() {
        if (rightSpinnerSkippedLiveData.value == true) {
            return
        }
        resetRightSpinnerIfNeeded()
        for (calculationLine in calculationLineList) {
            convert(calculationLine)
        }
        calculationLineListLiveData.postValue(calculationLineList)
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
        val current = calculationLineList[calculationLineList.size - 1]
        if (current.operator == "=") {
            return
        }
        current.operand += operand
        calculationLineListLiveData.postValue(calculationLineList)
    }

    fun decimal() {
        val current = calculationLineList[calculationLineList.size - 1]
        if (current.operator == "=") {
            return
        }
        if (!current.operand.contains(".")) {
            current.operand += "."
        }
        calculationLineListLiveData.postValue(calculationLineList)
    }

    fun backspace() {
        val current = calculationLineList[calculationLineList.size - 1]
        if (current.operator == "=") {
            return
        }
        val operand = current.operand
        if (operand.isNotEmpty()) {
            current.operand = operand.substring(0, operand.length - 1)
        }
        calculationLineListLiveData.postValue(calculationLineList)
    }

    fun operator(operator: String) {
        val current = calculationLineList[calculationLineList.size - 1]
        if ((current.operator == null && current.operand.isEmpty()) || current.operand == ".") {
            return
        }
        current.operand = stripTrailingZeros(current.operand)
        if (current.operand.isEmpty()) {
            current.operator = operator
        } else {
            if (calculationLineList.size > 1 && current.operator != "=") {
                val previous = calculationLineList[calculationLineList.size - 2]
                current.subtotal = compute(previous, current)
            } else {
                current.subtotal = current.operand
            }
            current.last = false
            calculationLineList.add(CalculationLine(0, 0, operator, "", "0", null, true))
        }
        convert(current)
        calculationLineListLiveData.postValue(calculationLineList)
    }

    fun equal() {
        var current = calculationLineList[calculationLineList.size - 1]
        var next: CalculationLine? = null
        if ((current.operator == null && current.operand.isEmpty()) || current.operand == ".") {
            return
        }
        current.operand = stripTrailingZeros(current.operand)
        if (current.operand.isEmpty()) {
            if (calculationLineList.size > 1) {
                val previous = calculationLineList[calculationLineList.size - 2]
                current.operator = "="
                current.operand = previous.subtotal
                current.subtotal = previous.subtotal
            }
        } else {
            if (calculationLineList.size > 1) {
                val previous = calculationLineList[calculationLineList.size - 2]
                current.subtotal = compute(previous, current)
            } else {
                current.subtotal = current.operand
            }
            current.last = false
            next = CalculationLine(0, 0, "=", current.subtotal, current.subtotal, null, true)
            calculationLineList.add(next)
        }
        convert(current)
        convert(next)
        calculationLineListLiveData.postValue(calculationLineList)
    }

    fun remove(index: Int) {
        removing.value = true
        calculationLineList.removeAt(index)
        if (calculationLineList.size == 0) {
            calculationLineList.add(CalculationLine(0, 0, null, "", "0", null, true))
        }
        var current: CalculationLine? = null
        for (i in 0 until calculationLineList.size) {
            current = calculationLineList[i]
            if (i == 0) {
                current.subtotal = current.operand
                current.operator = null
            } else if (current.operand.isNotEmpty()) {
                val previous = calculationLineList[i - 1]
                current.subtotal = compute(previous, current)
                if (current.operator == "=") {
                    current.operand = current.subtotal
                }
            }
            current.last = i == calculationLineList.size - 1
        }
        convert(current)
        calculationLineListLiveData.postValue(calculationLineList)
        Handler(Looper.getMainLooper()).postDelayed({
            removing.value = false
        }, 100)
    }

    fun clear() {
        calculationLineList.clear()
        calculationLineList.add(CalculationLine(0, 0, null, "", "0", null, true))
        calculationLineListLiveData.postValue(calculationLineList)
    }

    fun shouldShowErrorMessage(spinnerSkipped: Boolean): Boolean {
        return spinnerSkipped && !isRightSpinnerFirstItemSelected()
    }

    fun shouldScrollToBottom(): Boolean {
        return removing.value == false
    }
}