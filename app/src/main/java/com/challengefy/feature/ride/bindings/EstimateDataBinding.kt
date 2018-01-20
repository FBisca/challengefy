package com.challengefy.feature.ride.bindings

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import com.challengefy.data.model.Estimate
import com.challengefy.feature.ride.adapter.EstimateAdapter

@BindingAdapter("items")
fun bindEstimateItems(recyclerView: RecyclerView, items: List<Estimate>?) {
    if (items == null) return

    val adapter = recyclerView.adapter as EstimateAdapter?
    adapter?.setItems(items)
}
