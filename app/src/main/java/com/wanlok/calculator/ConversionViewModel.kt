package com.wanlok.calculator

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wanlok.calculator.model.ItemHeader
import com.wanlok.calculator.model.ItemLine

class ConversionViewModel: ViewModel() {
    val lll = arrayListOf(
        ItemHeader("Length"),
        ItemLine("cm"),
        ItemLine("m"),
        ItemLine("ft"),
        ItemHeader("Area"),
        ItemLine("m²"),
        ItemLine("ft²"),
        ItemHeader("Length"),
        ItemLine("cm"),
        ItemLine("m"),
        ItemLine("ft"),
        ItemHeader("Area"),
        ItemLine("m²"),
        ItemLine("ft²"),
        ItemHeader("Length"),
        ItemLine("cm"),
        ItemLine("m"),
        ItemLine("ft"),
        ItemHeader("Area"),
        ItemLine("m²"),
        ItemLine("ft²"),
        ItemHeader("Length"),
        ItemLine("cm"),
        ItemLine("m"),
        ItemLine("ft"),
        ItemHeader("Area"),
        ItemLine("m²"),
        ItemLine("ft²"),
        ItemHeader("Length"),
        ItemLine("cm"),
        ItemLine("m"),
        ItemLine("ft"),
        ItemHeader("Area"),
        ItemLine("m²"),
        ItemLine("ft²"),
        ItemHeader("Length"),
        ItemLine("cm"),
        ItemLine("m"),
        ItemLine("ft"),
        ItemHeader("Area"),
        ItemLine("m²"),
        ItemLine("ft²"),
        ItemHeader("Length"),
        ItemLine("cm"),
        ItemLine("m"),
        ItemLine("ft"),
        ItemHeader("Area"),
        ItemLine("m²"),
        ItemLine("ft²"),
        ItemHeader("Length"),
        ItemLine("cm"),
        ItemLine("m"),
        ItemLine("ft"),
        ItemHeader("Area"),
        ItemLine("m²"),
        ItemLine("ft²"),
        ItemHeader("Length"),
        ItemLine("cm"),
        ItemLine("m"),
        ItemLine("ft"),
        ItemHeader("Area"),
        ItemLine("m²"),
        ItemLine("ft²"),
    )

    val itemLines = MutableLiveData(lll)

    fun dummy(position: Int) {
//        Handler(Looper.getMainLooper()).postDelayed({
            lll[position].selected?.let { selected ->
                lll[position].selected = !selected
            }
            itemLines.postValue(lll)
//        }, 400)
    }
}