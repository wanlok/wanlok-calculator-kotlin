package com.wanlok.calculator.customView

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wanlok.calculator.databinding.ItemHeaderBinding
import com.wanlok.calculator.databinding.ItemListBinding
import com.wanlok.calculator.model.ConversionLine
import com.wanlok.calculator.model.ItemModel

class ListRecyclerAdapter(
    private val itemsList: List<ItemModel>,
    private val onHeaderItemClick: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    StickyHeaderItemDecorator.StickyHeaderInterface {

    companion object {
        const val TYPE_HEADER = 1
        const val TYPE_CONTENT = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(
                ItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> ItemBodyViewHolder(
                ItemListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.contentView.text = "Header $position"
                holder.nextButton.setOnClickListener { onHeaderItemClick(position) }
            }

            is ItemBodyViewHolder -> holder.contentView.text = "Content body at position $position"
        }
    }

    override fun getItemCount(): Int = itemsList.size

    override fun getItemViewType(position: Int) =
        if (itemsList[position].isHeader) TYPE_HEADER else TYPE_CONTENT

    inner class ItemBodyViewHolder(binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.content
    }

    inner class HeaderViewHolder(binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.content
        val nextButton: TextView = binding.nextText
    }

    override fun isHeader(itemPosition: Int) = itemsList[itemPosition].isHeader

}