package com.challengefy.address.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.challengefy.data.model.PredictionAddress
import com.challengefy.databinding.ItemAddressBinding

class AddressSearchAdapter : RecyclerView.Adapter<AddressSearchAdapter.ViewHolder>() {

    private val items = mutableListOf<PredictionAddress>()
    private var listener: (PredictionAddress) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAddressBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.address = items[position]
        holder.binding.executePendingBindings()
    }

    override fun getItemCount() = items.size

    fun setOnItemClick(listener: (PredictionAddress) -> Unit) {
        this.listener = listener
    }

    fun setItems(items: List<PredictionAddress>) {
        if (this.items.isEmpty()) {
            this.items.addAll(items)
            notifyItemRangeInserted(0, items.size)
        } else {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(
            val binding: ItemAddressBinding,
            listener: (PredictionAddress) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener(items[adapterPosition])
            }
        }
    }
}
