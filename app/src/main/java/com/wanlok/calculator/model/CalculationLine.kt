package com.wanlok.calculator.model

class CalculationLine(
    var calculationId: Int,
    var calculationLineId: Int,
    var operator: String?,
    var operand: String,
    var subtotal: String,
    var last: Boolean)