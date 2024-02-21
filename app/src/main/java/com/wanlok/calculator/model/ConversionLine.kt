package com.wanlok.calculator.model

data class ConversionLine(val text: String, val type: Int, val encode: (String) -> String, val decode: (String) -> String, var selected: Boolean)
