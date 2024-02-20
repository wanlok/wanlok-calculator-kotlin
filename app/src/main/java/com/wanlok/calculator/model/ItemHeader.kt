package com.wanlok.calculator.model

class ItemHeader(leftText: String, rightText: String?): ItemLine(true, leftText, rightText, null) {
    constructor(text: String): this(text, null)
}