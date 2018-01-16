package com.challengefy.feature.estimate.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.challengefy.R
import com.challengefy.data.model.Estimate
import com.challengefy.databinding.ItemEstimateBinding

class EstimateAdapter : RecyclerView.Adapter<EstimateAdapter.ViewHolder>() {

    private val items = mutableListOf<Estimate>()

    private var parent: RecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemEstimateBinding.inflate(inflater, parent, false))
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        parent = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        parent = null
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.estimate = items[position]
        holder.binding.executePendingBindings()

        val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
        val viewSize = holder.itemView.resources.getDimension(R.dimen.estimate_view_width).toInt()
        val parentWidth = parent?.width ?: 0

        if (position == 0) {
            params.marginStart = parentWidth / 2 - viewSize / 2
            params.marginEnd = 0
        } else if (position == itemCount - 1) {
            params.marginStart = 0
            params.marginEnd = parentWidth / 2 - viewSize / 2
        } else {
            params.marginStart = 0
            params.marginEnd = 0
        }
    }

    override fun getItemCount() = items.size

    fun setItems(estimates: List<Estimate>) {
        items.clear()
        items.addAll(estimates)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemEstimateBinding) : RecyclerView.ViewHolder(binding.root) {}
}
