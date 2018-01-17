package com.challengefy.feature.estimate.bindings

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import com.challengefy.data.model.Estimate
import com.challengefy.feature.estimate.adapter.EstimateAdapter

@BindingAdapter("items")
fun bindEstimateItems(recyclerView: RecyclerView, items: List<Estimate>?) {
    if (items == null) return

    val adapter = recyclerView.adapter as EstimateAdapter?
    adapter?.setItems(items)
}