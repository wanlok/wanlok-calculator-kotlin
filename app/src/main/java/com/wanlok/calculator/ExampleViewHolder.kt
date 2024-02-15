package com.wanlok.calculator

import androidx.recyclerview.widget.RecyclerView
import com.wanlok.calculator.databinding.ItemExampleBinding

class ExampleViewHolder(private val binding: ItemExampleBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ExampleItem) {
        binding.item = item
        binding.executePendingBindings()
    }
}