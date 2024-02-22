package com.wanlok.calculator.model

data class CalculationLine(
    var calculationId: Long,
    var calculationLineId: Long,
    var operator: String?,
    var operand: String,
    var subtotal: String,
    var convertedValue: String?,
    var last: Boolean
)