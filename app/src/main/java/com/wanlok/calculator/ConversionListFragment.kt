package com.wanlok.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wanlok.calculator.customView.StickyHeaderItemDecorator
import com.wanlok.calculator.customView.StickyHeaderRecyclerViewAdapter
import com.wanlok.calculator.databinding.FragmentCalculatorFilterListBinding
import com.wanlok.calculator.databinding.ItemHeaderBinding
import com.wanlok.calculator.databinding.ItemLineBinding
import com.wanlok.calculator.model.ItemHeader
import com.wanlok.calculator.model.ItemLine

class ConversionListFragment : NavigationFragment() {
    private lateinit var recyclerView: RecyclerView

    private var stickyHeaderItemDecorator: StickyHeaderItemDecorator? = null

    override fun getTitle(): String {
        return "Conversion List"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentCalculatorFilterListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calculator_filter_list, container, false)
//        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val view = binding.root
        recyclerView = view.findViewById(R.id.recyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.clipToPadding = false
        recyclerView.adapter = ConversionListRecyclerAdapter(getDummyItems())
        stickyHeaderItemDecorator = StickyHeaderItemDecorator.build(recyclerView, stickyHeaderItemDecorator)
    }

    private fun getDummyItems(): List<ItemLine> {
        return arrayListOf(
            ItemHeader("Length"),
            ItemLine("cm"),
            ItemLine("m"),
            ItemLine("ft"),
            ItemHeader("Area"),
            ItemLine("m²"),
            ItemLine("ft²"),
            ItemHeader("Length"),
            ItemLine("cm"),
            ItemLine("m"),
            ItemLine("ft"),
            ItemHeader("Area"),
            ItemLine("m²"),
            ItemLine("ft²"),
            ItemHeader("Length"),
            ItemLine("cm"),
            ItemLine("m"),
            ItemLine("ft"),
            ItemHeader("Area"),
            ItemLine("m²"),
            ItemLine("ft²"),
            ItemHeader("Length"),
            ItemLine("cm"),
            ItemLine("m"),
            ItemLine("ft"),
            ItemHeader("Area"),
            ItemLine("m²"),
            ItemLine("ft²"),
            ItemHeader("Length"),
            ItemLine("cm"),
            ItemLine("m"),
            ItemLine("ft"),
            ItemHeader("Area"),
            ItemLine("m²"),
            ItemLine("ft²"),
            ItemHeader("Length"),
            ItemLine("cm"),
            ItemLine("m"),
            ItemLine("ft"),
            ItemHeader("Area"),
            ItemLine("m²"),
            ItemLine("ft²"),
            ItemHeader("Length"),
            ItemLine("cm"),
            ItemLine("m"),
            ItemLine("ft"),
            ItemHeader("Area"),
            ItemLine("m²"),
            ItemLine("ft²"),
            ItemHeader("Length"),
            ItemLine("cm"),
            ItemLine("m"),
            ItemLine("ft"),
            ItemHeader("Area"),
            ItemLine("m²"),
            ItemLine("ft²"),
            ItemHeader("Length"),
            ItemLine("cm"),
            ItemLine("m"),
            ItemLine("ft"),
            ItemHeader("Area"),
            ItemLine("m²"),
            ItemLine("ft²"),
        )
    }

    class ConversionListRecyclerAdapter(private val itemLines: List<ItemLine>) : StickyHeaderRecyclerViewAdapter() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return when (viewType) {
                1 -> HeaderViewHolder(DataBindingUtil.inflate(inflater, R.layout.item_header, parent, false))
                else -> LineViewHolder(DataBindingUtil.inflate(inflater, R.layout.item_line, parent, false))
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is HeaderViewHolder -> holder.bind(itemLines[position])
                is LineViewHolder -> holder.bind(itemLines[position])
            }
        }

        override fun getItemCount(): Int {
            return itemLines.size
        }

        override fun getItemViewType(position: Int): Int {
            return if (itemLines[position].isHeader) 1 else 0
        }

        override fun isHeader(position: Int): Boolean {
            return itemLines[position].isHeader
        }

        inner class HeaderViewHolder(private val binding: ItemHeaderBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(itemLine: ItemLine) {
                binding.itemLine = itemLine
                binding.executePendingBindings()
            }
        }

        inner class LineViewHolder(private val binding: ItemLineBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(itemLine: ItemLine) {
                binding.itemLine = itemLine
                binding.executePendingBindings()
            }
        }
    }
}