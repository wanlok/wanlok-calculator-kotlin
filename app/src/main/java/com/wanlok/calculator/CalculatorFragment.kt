package com.wanlok.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanlok.calculator.customView.CalculatorButton

class CalculatorFragment : NavigationFragment() {
    private val presenter: A1Presenter? = null

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

    private lateinit var decimalButton: CalculatorButton
    private lateinit var addButton: CalculatorButton
    private lateinit var minusButton: CalculatorButton
    private lateinit var multiplyButton: CalculatorButton
    private lateinit var divideButton: CalculatorButton
    private lateinit var backspaceButton: CalculatorButton

    override fun getTitle(): String {
        return "Calculator"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calculator, null) as ViewGroup

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

        decimalButton = view.findViewById(R.id.decimalButton)
        addButton = view.findViewById(R.id.addButton)
        minusButton = view.findViewById(R.id.minusButton)
        multiplyButton = view.findViewById(R.id.multiplyButton)
        divideButton = view.findViewById(R.id.divideButton)
        backspaceButton = view.findViewById(R.id.backspaceButton)

//        fragmentCalculatorBinding.setViewModel(new LoginViewModel());
//        fragmentCalculatorBinding.executePendingBindings();

//        presenter = new A1Presenter(getArguments());
//
//        textView = root.findViewById(R.id.textView);
//        button = root.findViewById(R.id.button);
//
//        textView.setText(presenter.getName());
//
//        button.setText("Next");
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString("dummy", "ABCD");
//
//                A2Fragment fragment = new A2Fragment();
//                fragment.setArguments(bundle);
//
//                open(fragment, button);
//            }
//        });
        return view
    }
}

