package com.wanlok.calculator.model

open class ItemLine(var isHeader: Boolean, var leftText: String, var rightText: String?, var selected: Boolean?, var data: Any?) {
    constructor(text: String, selected: Boolean, data: Any): this(false, text, null, selected, data)
}
