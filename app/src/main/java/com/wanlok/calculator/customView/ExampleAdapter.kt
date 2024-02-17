package com.wanlok.calculator.customView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wanlok.calculator.R
import com.wanlok.calculator.databinding.ItemExampleBinding
import com.wanlok.calculator.model.CalculationLine

class ExampleAdapter(private var calculationLines: List<CalculationLine>): RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemExampleBinding = DataBindingUtil.inflate(inflater, R.layout.item_example, parent, false)
        return ExampleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        holder.bind(calculationLines[position])
    }

    override fun getItemCount(): Int {
        return calculationLines.size
    }

    fun updateList(calculationLines: List<CalculationLine>) {
        this.calculationLines = calculationLines
        notifyDataSetChanged()
    }

    class ExampleViewHolder(private val binding: ItemExampleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(calculationLine: CalculationLine) {
            binding.calculationLine = calculationLine
            binding.executePendingBindings()
        }
    }
}