package com.wanlok.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wanlok.calculator.customView.BindableSpinnerAdapter
import com.wanlok.calculator.customView.CalculatorButton
import com.wanlok.calculator.customView.ExampleAdapter
import com.wanlok.calculator.customView.SwipeListener
import com.wanlok.calculator.customView.SwipeSimpleCallback
import com.wanlok.calculator.databinding.FragmentCalculatorBinding

class NumberCalculatorFragment : NavigationFragment(), SwipeListener {
    private val viewModel: NumberCalculatorViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var leftSpinner: Spinner
    private lateinit var rightSpinner: Spinner

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

    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun getTitle(): String = "Number Calculator"

    private fun onNumberButtonClick(view: View) {
        var value = ""
        for (i in numberButtons.indices) {
            if (numberButtons[i].isClicked(view)) {
                value += i
                break
            }
        }
        viewModel.text(value)
    }

    private fun onDecimalButtonClick() {
        viewModel.decimal()
    }

    private fun onAddButtonClick() {
        viewModel.operator("+")
    }

    private fun onMinusButtonClick() {
        viewModel.operator("-")
    }

    private fun onMultiplyButtonClick() {
        viewModel.operator("×")
    }

    private fun onDivideButtonClick() {
        viewModel.operator("÷")
    }

    private fun onEqualButtonClick() {
        viewModel.equal()
    }

    private fun onBackspaceButtonClick() {
        viewModel.backspace()
    }

    private fun onClearButtonClick() {
        viewModel.clear()
    }

    override fun onSwipe(direction: Int, position: Int) {
        viewModel.remove(position)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)

        val binding = DataBindingUtil.inflate<FragmentCalculatorBinding>(inflater, R.layout.fragment_calculator, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val view = binding.root

        leftSpinner = view.findViewById(R.id.leftSpinner)
        rightSpinner = view.findViewById(R.id.rightSpinner)

        calculationRecyclerView = view.findViewById(R.id.calculationRecyclerView)
        calculationRecyclerView.layoutManager = LinearLayoutManager(activity)
        calculationRecyclerView.setPadding(0, Utils.dp(8, context), 0, Utils.dp(8, context))
        calculationRecyclerView.clipToPadding = false
        calculationRecyclerView.adapter = ExampleAdapter(emptyList())

        context?.let { context ->
            itemTouchHelper = ItemTouchHelper(SwipeSimpleCallback(context, this))
            itemTouchHelper.attachToRecyclerView(calculationRecyclerView)
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
        decimalButton.setOnClickListener { onDecimalButtonClick() }

        addButton = view.findViewById(R.id.addButton)
        addButton.setOnClickListener { onAddButtonClick() }

        minusButton = view.findViewById(R.id.minusButton)
        minusButton.setOnClickListener { onMinusButtonClick() }

        multiplyButton = view.findViewById(R.id.multiplyButton)
        multiplyButton.setOnClickListener { onMultiplyButtonClick() }

        divideButton = view.findViewById(R.id.divideButton)
        divideButton.setOnClickListener { onDivideButtonClick() }

        equalButton = view.findViewById(R.id.equalButton)
        equalButton.setOnClickListener { onEqualButtonClick() }

        backspaceButton = view.findViewById(R.id.backspaceButton)
        backspaceButton.setOnClickListener { onBackspaceButtonClick() }

        clearButton = view.findViewById(R.id.clearButton)
        clearButton.setOnClickListener { onClearButtonClick() }

        viewModel.leftSpinnerSelectedItemLiveData.observe(viewLifecycleOwner) {
            viewModel.leftSpinner()
        }

        viewModel.rightSpinnerSelectedItemLiveData.observe(viewLifecycleOwner) {
            viewModel.rightSpinner()
        }

        viewModel.leftSpinnerSkippedLiveData.observe(viewLifecycleOwner) {
            if (viewModel.shouldShowErrorMessage(it)) {
                Toast.makeText(context, getString(R.string.invalid_conversion), Toast.LENGTH_LONG).show()
            }
        }

        viewModel.rightSpinnerSkippedLiveData.observe(viewLifecycleOwner) {
            if (viewModel.shouldShowErrorMessage(it)) {
                Toast.makeText(context, getString(R.string.invalid_conversion), Toast.LENGTH_LONG).show()
            }
        }

        viewModel.calculationLineListLiveData.observe(viewLifecycleOwner) { lines ->
            val adapter = calculationRecyclerView.adapter as ExampleAdapter
            adapter.updateList(lines)
            if (viewModel.shouldScrollToBottom()) {
                calculationRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        }

        viewModel.setup(
            sharedViewModel.leftFirstConversionLine,
            sharedViewModel.rightFirstConversionLine,
            sharedViewModel.conversionLineLiveData.value
        )

        sharedViewModel.conversionLineLiveData.observe(viewLifecycleOwner) { conversionLines ->
            viewModel.setup(
                sharedViewModel.leftFirstConversionLine,
                sharedViewModel.rightFirstConversionLine,
                conversionLines
            )
            (leftSpinner.adapter as? BindableSpinnerAdapter)?.update(viewModel.leftSpinnerItemList)
            (rightSpinner.adapter as? BindableSpinnerAdapter)?.update(viewModel.rightSpinnerItemList)
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_filter -> {
                open(ConversionFragment())
                return true
            }
        }
        return false
    }
}

