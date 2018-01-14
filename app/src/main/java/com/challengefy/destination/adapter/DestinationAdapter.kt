package com.challengefy.destination.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.challengefy.data.model.Address
import com.challengefy.databinding.ItemAddressBinding

class DestinationAdapter : RecyclerView.Adapter<DestinationAdapter.ViewHolder>() {

    val items = mutableListOf<Address>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAddressBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.address = items[position]
        holder.binding.executePendingBindings()
    }

    override fun getItemCount() = items.size

    class ViewHolder(val binding: ItemAddressBinding) : RecyclerView.ViewHolder(binding.root)
}
