package com.wanlok.calculator.model

open class ItemLine(var isHeader: Boolean, var leftText: String, var rightText: String?, var selected: Boolean?) {
    constructor(text: String): this(false, text, null, false)
}