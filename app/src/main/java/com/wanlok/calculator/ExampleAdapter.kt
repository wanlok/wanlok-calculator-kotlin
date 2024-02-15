package com.wanlok.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wanlok.calculator.databinding.ItemExampleBinding

class ExampleAdapter(private var items: List<ExampleItem>) : RecyclerView.Adapter<ExampleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemExampleBinding = DataBindingUtil.inflate(inflater, R.layout.item_example, parent, false)
        return ExampleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateList(items: List<ExampleItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}