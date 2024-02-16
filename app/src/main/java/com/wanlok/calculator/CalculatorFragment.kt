package com.wanlok.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wanlok.calculator.customView.CalculatorButton
import com.wanlok.calculator.databinding.FragmentCalculatorBinding

class CalculatorFragment : NavigationFragment() {
    private val viewModel: CalculatorViewModel by viewModels()

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
    private lateinit var clearButton: CalculatorButton

    override fun getTitle(): String {
        return "Number Calculator"
    }

    private fun onNumberButtonClick(view: View) {
        var value = ""
        for (i in numberButtons.indices) {
            if (numberButtons[i].isClicked(view)) {
                value += i
                break
            }
        }
        value?.let { viewModel.text(it) }
    }

    private fun onDecimalButtonClick(view: View) {
        viewModel.decimal()
    }

    private fun onAddButtonClick(view: View) {
        viewModel.operator("+")
    }

    private fun onMinusButtonClick(view: View) {
        viewModel.operator("-")
    }

    private fun onMultiplyButtonClick(view: View) {
        viewModel.operator("×")
    }

    private fun onDivideButtonClick(view: View) {
        viewModel.operator("÷")
    }

    private fun onEqualButtonClick(view: View) {
        viewModel.equal()
    }

    private fun onBackspaceButtonClick(view: View) {
        viewModel.backspace()
    }

    private fun onClearButtonClick(view: View) {
        viewModel.clear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentCalculatorBinding>(inflater, R.layout.fragment_calculator, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val view = binding.root

        calculationRecyclerView = view.findViewById(R.id.calculationRecyclerView)
        calculationRecyclerView.layoutManager = LinearLayoutManager(activity)
        calculationRecyclerView.setPadding(0, 0, 0, Utils.dp(16, context))
        calculationRecyclerView.clipToPadding = false;
        calculationRecyclerView.adapter = ExampleAdapter(emptyList())

        viewModel.lines.observe(viewLifecycleOwner) { lines ->
            val adapter = calculationRecyclerView.adapter as ExampleAdapter
            adapter.updateList(lines)
            calculationRecyclerView.scrollToPosition(adapter.itemCount - 1);
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

        clearButton = view.findViewById(R.id.clearButton)
        clearButton.setOnClickListener { onClearButtonClick(it) }

        return view
    }
}

