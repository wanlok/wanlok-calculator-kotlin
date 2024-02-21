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
    val allCheckedLiveData = MutableLiveData(false)
    val manuallyCheckedLiveData = MutableLiveData(false)

    fun setup(conversions: List<Conversion>?, conversionLines: List<ConversionLine>?) {
        conversions?.let { conversions ->
            conversionLines?.let { conversionLines ->
                val itemLineList = ArrayList<ItemLine>()
                for (conversion in conversions) {
                    itemLineList.add(ItemHeader(conversion.text))
                    for (conversionLine in conversionLines) {
                        if (conversion.type == conversionLine.type) {
                            itemLineList.add(ItemLine(conversionLine.text, conversionLine.selected, conversionLine))
                        }
                    }
                }
                this.itemLineList = itemLineList
                itemLineListLiveData.postValue(itemLineList)
            }
        }
    }

    fun isAllChecked(): Boolean {
        var allChecked = true
        for (position in itemLineList.indices) {
            if ((itemLineList[position].data as? ConversionLine)?.selected == false) {
                allChecked = false
                break
            }
        }
        return allChecked
    }

    fun check(position: Int) {
        itemLineList[position].let { itemLine ->
            itemLine.selected?.let { selected ->
                (itemLine.data as ConversionLine).selected = !selected
                itemLine.selected = !selected
            }
        }
        itemLineListLiveData.postValue(itemLineList)
    }

    fun checkAll(selected: Boolean) {
        if (manuallyCheckedLiveData.value == true) {
            for (position in itemLineList.indices) {
                itemLineList[position].let { itemLine ->
                    (itemLine.data as? ConversionLine)?.selected = selected
                    itemLine.selected = selected
                }
            }
            itemLineListLiveData.postValue(itemLineList)
        }
    }
}