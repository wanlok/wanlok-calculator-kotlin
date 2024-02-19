package com.wanlok.calculator.model

import java.util.Date

data class ConversionLine (
    var conversionLine: Int,
    var unit: String,
    var datetime: Date?,
    var isHeader: Boolean,
    var selected: Boolean
)