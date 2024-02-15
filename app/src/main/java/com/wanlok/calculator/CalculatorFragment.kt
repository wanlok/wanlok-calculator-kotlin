package com.wanlok.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wanlok.calculator.customView.CalculatorButton
import com.wanlok.calculator.databinding.FragmentCalculatorBinding

class CalculatorFragment : NavigationFragment() {
    private val viewModel: CalculationViewModel by viewModels()

    private lateinit var calculationRecyclerView: RecyclerView

    private lateinit var zeroButton: CalculatorButton
    private lateinit var oneButton: CalculatorButton
    private lateinit var twoButton: CalculatorButton
    private lateinit var threeButton: CalculatorButton
    private lateinit var fourButton: CalculatorButton
    private lateinit var fiveButton: CalculatorButton
    private lateinit var sixButton: CalculatorButton
    private lateinit var sevenButton: CalculatorButton
    private lateinit var eightButton: CalculatorButton
    private lateinit var nineButton: CalculatorButton

    private lateinit var numberButtons: List<CalculatorButton>

    private lateinit var decimalButton: CalculatorButton
    private lateinit var addButton: CalculatorButton
    private lateinit var minusButton: CalculatorButton
    private lateinit var multiplyButton: CalculatorButton
    private lateinit var divideButton: CalculatorButton
    private lateinit var equalButton: CalculatorButton
    private lateinit var backspaceButton: CalculatorButton

    override fun getTitle(): String {
        return "Calculator"
    }

    private fun onNumberButtonClick(view: View) {
        var value: Int? = null
        for (i in numberButtons.indices) {
            if (numberButtons[i].isClicked(view)) {
                value = i
                break
            }
        }
        value?.let { viewModel.getUpdatedText(it) }
    }

    private fun dummy() {
        viewModel.add()
    }

    private fun onDecimalButtonClick(view: View) {
        dummy()
    }

    private fun onAddButtonClick(view: View) {
        dummy()
    }

    private fun onMinusButtonClick(view: View) {
        dummy()
    }

    private fun onMultiplyButtonClick(view: View) {
        dummy()
    }

    private fun onDivideButtonClick(view: View) {
        dummy()
    }

    private fun onEqualButtonClick(view: View) {
        dummy()
    }

    private fun onBackspaceButtonClick(view: View) {
        viewModel.backspace()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentCalculatorBinding>(inflater, R.layout.fragment_calculator, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val view = binding.root

        calculationRecyclerView = view.findViewById(R.id.calculationRecyclerView)
        calculationRecyclerView.layoutManager = LinearLayoutManager(activity)
        calculationRecyclerView.adapter = ExampleAdapter(emptyList())

        viewModel.values.observe(viewLifecycleOwner) { newList ->
            val adapter: ExampleAdapter = calculationRecyclerView.adapter as ExampleAdapter
            adapter.updateList(newList)
        }

        zeroButton = view.findViewById(R.id.zeroButton)
        oneButton = view.findViewById(R.id.oneButton)
        twoButton = view.findViewById(R.id.twoButton)
        threeButton = view.findViewById(R.id.threeButton)
        fourButton = view.findViewById(R.id.fourButton)
        fiveButton = view.findViewById(R.id.fiveButton)
        sixButton = view.findViewById(R.id.sixButton)
        sevenButton = view.findViewById(R.id.sevenButton)
        eightButton = view.findViewById(R.id.eightButton)
        nineButton = view.findViewById(R.id.nineButton)

        numberButtons = listOf(zeroButton, oneButton, twoButton, threeButton, fourButton, fiveButton, sixButton, sevenButton, eightButton, nineButton)

        for (numberButton in numberButtons) {
            numberButton.setOnClickListener { onNumberButtonClick(it) }
        }

        decimalButton = view.findViewById(R.id.decimalButton)
        decimalButton.setOnClickListener { onDecimalButtonClick(it) }

        addButton = view.findViewById(R.id.addButton)
        addButton.setOnClickListener { onAddButtonClick(it) }

        minusButton = view.findViewById(R.id.minusButton)
        minusButton.setOnClickListener { onMinusButtonClick(it) }

        multiplyButton = view.findViewById(R.id.multiplyButton)
        multiplyButton.setOnClickListener { onMultiplyButtonClick(it) }

        divideButton = view.findViewById(R.id.divideButton)
        divideButton.setOnClickListener { onDivideButtonClick(it) }

        equalButton = view.findViewById(R.id.equalButton)
        equalButton.setOnClickListener { onEqualButtonClick(it) }

        backspaceButton = view.findViewById(R.id.backspaceButton)
        backspaceButton.setOnClickListener { onBackspaceButtonClick(it) }

        return view
    }
}

