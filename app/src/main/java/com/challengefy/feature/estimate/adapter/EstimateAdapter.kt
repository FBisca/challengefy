package com.challengefy.feature.estimate.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.challengefy.data.model.Estimate
import com.challengefy.databinding.ItemEstimateBinding

class EstimateAdapter : RecyclerView.Adapter<EstimateAdapter.ViewHolder>() {

    private val items = mutableListOf<Estimate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemEstimateBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.estimate = items[position]
        holder.binding.executePendingBindings()
    }

    override fun getItemCount() = items.size

    fun setItems(estimates: List<Estimate>) {
        items.clear()
        items.addAll(estimates)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemEstimateBinding) : RecyclerView.ViewHolder(binding.root)
}
