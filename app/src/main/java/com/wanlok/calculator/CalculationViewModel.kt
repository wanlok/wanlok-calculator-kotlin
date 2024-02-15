package com.wanlok.calculator

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date

class CalculationViewModel: ViewModel() {

    private val calculation = Calculation(0, Date(), "")

    val uiTextLiveData = MutableLiveData<String>()

    fun getUpdatedText(value: Int) {
        calculation.value = calculation.value + " " + value
        Log.d("ROBERT", "CHECK TEXT: " + calculation.value)
        uiTextLiveData.postValue(calculation.value)
    }
}