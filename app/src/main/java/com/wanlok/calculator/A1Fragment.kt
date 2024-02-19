package com.wanlok.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wanlok.calculator.customView.ExampleAdapter
import com.wanlok.calculator.customView.ListRecyclerAdapter
import com.wanlok.calculator.customView.StickyHeaderItemDecorator
import com.wanlok.calculator.databinding.FragmentCalculatorBinding
import com.wanlok.calculator.databinding.FragmentCalculatorFilterListBinding
import com.wanlok.calculator.databinding.ItemConversionBinding
import com.wanlok.calculator.databinding.ItemExampleBinding
import com.wanlok.calculator.model.CalculationLine
import com.wanlok.calculator.model.ConversionLine
import com.wanlok.calculator.model.ItemModel

class A1Fragment : NavigationFragment() {
    private lateinit var recyclerView: RecyclerView

    private var stickyHeaderItemDecorator: StickyHeaderItemDecorator? = null

    override fun getTitle(): String {
        return "Conversion List"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DataBindingUtil.inflate<FragmentCalculatorFilterListBinding>(inflater, R.layout.fragment_calculator_filter_list, container, false)
//        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val view = binding.root

//        val conversionLines = listOf(
//            ConversionLine(0, "A", null, false),
//            ConversionLine(0, "B", null, false),
//            ConversionLine(0, "C", null, false),
//            ConversionLine(0, "D", null, false),
//            ConversionLine(0, "E", null, true)
//        )

        recyclerView = view.findViewById(R.id.recyclerView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView.layoutManager = LinearLayoutManager(activity)
//        recyclerView.setPadding(0, Utils.dp(8, context), 0, Utils.dp(8, context))
        recyclerView.clipToPadding = false
//        recyclerView.adapter = Adapter(conversionLines)
        recyclerView.adapter = ListRecyclerAdapter(getDummyItems()) {

        }

        initStickyItemDecorator()
    }

    private fun getDummyItems(): List<ItemModel> {
        return arrayListOf(
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = true),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = true),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = true),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = true),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = true),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = true),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = true),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = true),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = true),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = true),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = true),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = true),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = true),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = true),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false),
            ItemModel(isHeader = false)
        )
    }

    private fun initStickyItemDecorator() {
        stickyHeaderItemDecorator?.clearReferences()
        stickyHeaderItemDecorator = null
        stickyHeaderItemDecorator = StickyHeaderItemDecorator()
        val adapter = recyclerView.adapter
        if (adapter is ListRecyclerAdapter) {
            stickyHeaderItemDecorator?.attachRecyclerView(adapter, recyclerView, adapter)
        }
    }

//    class Adapter(private var conversionLines: List<ConversionLine>): RecyclerView.Adapter<Adapter.ViewHolder>() {
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//            val inflater = LayoutInflater.from(parent.context)
//            val binding: ItemConversionBinding = DataBindingUtil.inflate(inflater, R.layout.item_conversion, parent, false)
//            return ViewHolder(binding)
//        }
//
//        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//            viewHolder.bind(conversionLines[position])
//        }
//
//        override fun getItemCount(): Int {
//            return conversionLines.size
//        }
//
//        fun update(conversionLines: List<ConversionLine>) {
//            this.conversionLines = conversionLines
//            notifyDataSetChanged()
//        }
//
//        class ViewHolder(private val binding: ItemConversionBinding): RecyclerView.ViewHolder(binding.root) {
//            fun bind(conversionLine: ConversionLine) {
//                binding.conversionLine = conversionLine
//                binding.executePendingBindings()
//            }
//        }
//    }
}