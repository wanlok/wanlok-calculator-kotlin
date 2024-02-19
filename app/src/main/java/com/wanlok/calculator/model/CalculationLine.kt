package com.wanlok.calculator.model

data class CalculationLine(
    var calculationId: Int,
    var calculationLineId: Int,
    var operator: String?,
    var operand: String,
    var subtotal: String,
    var convertedValue: String?,
    var last: Boolean
)