package com.wanlok.calculator

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wanlok.calculator.customView.SeparatorItemDecoration
import com.wanlok.calculator.customView.StickyHeaderItemDecorator
import com.wanlok.calculator.customView.StickyHeaderRecyclerViewAdapter
import com.wanlok.calculator.databinding.FragmentConversionBinding
import com.wanlok.calculator.databinding.ItemHeaderBinding
import com.wanlok.calculator.databinding.ItemLineBinding
import com.wanlok.calculator.model.ItemLine

class ConversionFragment : NavigationFragment() {
    private val viewModel: ConversionViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var titleCheckBox: CheckBox
    private lateinit var titleTextView: TextView
    private lateinit var recyclerView: RecyclerView

    private var stickyHeaderItemDecorator: StickyHeaderItemDecorator? = null

    override fun getTitle(): String = "Conversion"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)

        val binding = DataBindingUtil.inflate<FragmentConversionBinding>(inflater, R.layout.fragment_conversion, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val view = binding.root

        parentView = view.findViewById(R.id.parentView)
        titleCheckBox = view.findViewById(R.id.titleCheckBox)
        titleTextView = view.findViewById(R.id.titleTextView)
        recyclerView = view.findViewById(R.id.recyclerView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preventParentViewClickable()

        titleCheckBox.setOnCheckedChangeListener { _, checked ->
            if (viewModel.manuallyCheckedLiveData.value == true) {
                viewModel.checkAll(checked)
            }
        }

        titleTextView.setOnClickListener {
            titleCheckBox.performClick()
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.clipToPadding = false

        val separatorHeight = resources.getDimensionPixelSize(R.dimen.separator_height) // Define your separator height

        context?.let { context ->
            val separatorItemDecoration = SeparatorItemDecoration(context, separatorHeight)
            recyclerView.addItemDecoration(separatorItemDecoration)
        }

        viewModel.setup(
            sharedViewModel.conversionLiveData.value,
            sharedViewModel.conversionLineLiveData.value
        )

        recyclerView.adapter = ConversionRecyclerViewAdapter(viewModel.itemLineList) { position ->
            viewModel.check(position)
        }

        stickyHeaderItemDecorator = StickyHeaderItemDecorator.build(recyclerView, stickyHeaderItemDecorator)

        viewModel.itemLineListLiveData.observe(viewLifecycleOwner) { itemLines ->
            (recyclerView.adapter as ConversionRecyclerViewAdapter).update(itemLines)
            sharedViewModel.update()
            viewModel.manuallyCheckedLiveData.value = false
            titleCheckBox.isChecked = viewModel.isAllChecked()
            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.manuallyCheckedLiveData.value = true
            }, 100)
        }
    }

    class ConversionRecyclerViewAdapter(private var itemLines: List<ItemLine>, private val onClick: (Int) -> Unit) : StickyHeaderRecyclerViewAdapter() {
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

        fun update(itemLines: List<ItemLine>) {
            this.itemLines = itemLines
            notifyDataSetChanged()
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
                binding.root.setOnClickListener {
                    onClick(adapterPosition)
                }
                binding.executePendingBindings()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }
}