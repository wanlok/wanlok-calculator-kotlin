package com.wanlok.calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wanlok.calculator.model.Conversion
import com.wanlok.calculator.model.ConversionLine
import com.wanlok.calculator.model.ItemHeader
import com.wanlok.calculator.model.ItemLine

class ConversionViewModel: ViewModel() {
    var itemLineList: List<ItemLine> = emptyList()
    val itemLineListLiveData = MutableLiveData(itemLineList)

    fun setup(conversions: List<Conversion>?, conversionLines: List<ConversionLine>?) {
        conversions?.let { conversions ->
            conversionLines?.let { conversionLines ->
                val itemLines = ArrayList<ItemLine>()
                for (conversion in conversions) {
                    itemLines.add(ItemHeader(conversion.text))
                    for (conversionLine in conversionLines) {
                        if (conversion.type == conversionLine.type) {
                            itemLines.add(ItemLine(conversionLine.text, conversionLine.selected, conversionLine))
                        }
                    }
                }
                this.itemLineList = itemLines
                itemLineListLiveData.value = itemLines
            }
        }
    }

    fun setItemLineSelected(position: Int) {
       itemLineList[position]?.let { itemLine ->
            itemLine.selected?.let { selected ->
                (itemLine.data as ConversionLine).selected = !selected
                itemLine.selected = !selected
            }
        }
        itemLineListLiveData.postValue(itemLineList)
    }
}