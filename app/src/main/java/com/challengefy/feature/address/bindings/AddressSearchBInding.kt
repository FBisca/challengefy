package com.challengefy.feature.address.bindings

import android.databinding.BindingAdapter
import android.support.transition.ChangeBounds
import android.support.transition.TransitionManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.challengefy.data.model.PredictionAddress
import com.challengefy.feature.address.adapter.AddressSearchAdapter

@BindingAdapter("items")
fun bindAddressSearchItems(recyclerView: RecyclerView, items: List<PredictionAddress>?) {
    if (items == null) return

    TransitionManager.beginDelayedTransition(recyclerView.parent as ViewGroup, ChangeBounds()
            .addTarget(recyclerView)
    )

    val adapter = recyclerView.adapter as AddressSearchAdapter?
    adapter?.setItems(items)
}
