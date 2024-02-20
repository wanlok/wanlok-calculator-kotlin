package com.wanlok.calculator.model

import java.util.Date

open class ItemLine(var isHeader: Boolean, var text: String, var datetime: Date?, var selected: Boolean?) {
    constructor(text: String): this(false, text, null, false)
}