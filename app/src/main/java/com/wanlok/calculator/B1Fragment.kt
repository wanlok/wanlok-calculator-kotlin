package com.wanlok.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


class B1Fragment : NavigationFragment() {
    private var presenter: B1Presenter? = null
    private var textView: TextView? = null
    private var button: Button? = null

    override fun getTitle(): String {
        return "B1"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_b1, null) as ViewGroup
        presenter = B1Presenter()
        textView = root.findViewById<TextView>(R.id.textView)
        button = root.findViewById<Button>(R.id.button)
        textView?.text = "B1"
        button?.text = "Next"
//        button.setOnClickListener(View.OnClickListener { })
        return root
    }
}

