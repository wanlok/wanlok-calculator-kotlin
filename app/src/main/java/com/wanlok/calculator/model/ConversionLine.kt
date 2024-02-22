package com.wanlok.calculator.model

data class ConversionLine(val id: String, val conversionId: String, val conversionOrder: Long, val text: String, val encode: String, val decode: String, var selected: Boolean)
