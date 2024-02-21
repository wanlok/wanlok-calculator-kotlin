package com.wanlok.calculator

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wanlok.calculator.Utils.divide
import com.wanlok.calculator.Utils.multiply
import com.wanlok.calculator.model.Conversion
import com.wanlok.calculator.model.ConversionLine

class SharedViewModel : ViewModel() {
    val leftFirstConversionLine = ConversionLine("Numbers", 0, { x: String -> x }, { x: String -> x }, true)
    val rightFirstConversionLine = ConversionLine("Subtotal", 0, { x: String -> x }, { x: String -> x }, true)

    private val conversions = listOf(
        Conversion(1, "Length"),
        Conversion(2, "Area"),
        Conversion(3, "Currency")
    )

    private val conversionLines = listOf(
        ConversionLine("cm", 1, { x: String -> x }, { x: String -> x }, true),
        ConversionLine("m", 1, { x: String -> multiply(x, "100") }, { x: String -> divide(x, "100") }, true),
        ConversionLine("ft", 1, { x: String -> multiply(x, "30.48") }, { x: String -> divide(x, "30.48") }, true),
        ConversionLine("m²", 2, { x: String -> x }, { x: String -> x }, true),
        ConversionLine("ft²", 2, { x: String -> multiply(x, "0.09290304") }, { x: String -> divide(x, "0.09290304") }, true),
    )

    val conversionLiveData = MutableLiveData(conversions)
    val conversionLineLiveData = MutableLiveData(conversionLines)

    fun update() {
        conversionLineLiveData.postValue(conversionLines)
    }
}