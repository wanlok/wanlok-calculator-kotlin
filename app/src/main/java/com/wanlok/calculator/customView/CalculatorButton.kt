package com.wanlok.calculator.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import com.wanlok.calculator.R

class CalculatorButton (context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private var button: Button

    private var buttonText: String = ""
        set(value) {
            field = value
            button.text = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_calculator_button, this, true)
        orientation = VERTICAL
        button = findViewById(R.id.button)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CalculatorButton)
//            buttonText = resources.getString(typedArray.getResourceId(R.styleable.CalculatorButton_text, R.string.defaultString))
            typedArray.getString(R.styleable.CalculatorButton_text)?.let { buttonText ->
                this.buttonText = buttonText
            }
            typedArray.recycle()
        }
    }
}