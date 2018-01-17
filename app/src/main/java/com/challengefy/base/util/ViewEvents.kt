package com.challengefy.base.util

import android.graphics.Rect
import android.view.View
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

fun View.boundsChangeEvents(): Flowable<Rect> {
    return Flowable.create({ emitter ->
        val listener = View.OnLayoutChangeListener { _, left, top, right, bottom, _, _, _, _ ->
            emitter.onNext(Rect(left, top, right, bottom))
        }
        addOnLayoutChangeListener(listener)
        emitter.setCancellable { removeOnLayoutChangeListener(listener) }
    }, BackpressureStrategy.LATEST)
}