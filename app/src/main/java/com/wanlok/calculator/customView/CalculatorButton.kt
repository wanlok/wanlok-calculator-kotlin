package com.wanlok.calculator.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.wanlok.calculator.R

class CalculatorButton (context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private var button: Button

    private var text: String = ""
        set(value) {
            field = value
            button.text = value
        }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        button.setOnClickListener(onClickListener)
    }

    fun isClicked(view: View): Boolean {
        return view == this.button
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_calculator_button, this, true)
        orientation = VERTICAL
        button = findViewById(R.id.button)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CalculatorButton)
//            buttonText = resources.getString(typedArray.getResourceId(R.styleable.CalculatorButton_text, R.string.defaultString))
            typedArray.getString(R.styleable.CalculatorButton_text)?.let { text ->
                this.text = text
            }
            typedArray.recycle()
        }
    }
}