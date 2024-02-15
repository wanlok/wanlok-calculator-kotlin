package com.wanlok.calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wanlok.calculator.model.Calculation
import java.util.Date

class CalculationViewModel: ViewModel() {

    private val calculation = Calculation(0, Date(), "")

    val current = MutableLiveData<String>()
    val values = MutableLiveData<ArrayList<ExampleItem>>()

    init {
        current.value = ""
        values.value = ArrayList()
    }

    fun getUpdatedText(value: Int) {
//        calculation.value = calculation.value + value
//        Log.d("ROBERT", "CHECK TEXT: " + calculation.value)

        current.value = current.value + value
        current.postValue(current.value)
    }

    fun backspace() {
        current.value?.let { value ->
            if (value.isNotEmpty()) {
                current.postValue(value.substring(0, value.length - 1))
            }
        }
    }

    fun add() {
        values.value?.let { values ->
            current.value?.let { value ->
                if (value.isNotEmpty()) {
                    values.add(ExampleItem(value))
                    current.postValue("")
                }
            }
            this.values.postValue(values)
        }
    }
}