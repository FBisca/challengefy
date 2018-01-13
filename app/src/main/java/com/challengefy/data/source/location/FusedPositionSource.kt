package com.challengefy.data.source.location

import android.content.Context
import com.challengefy.data.model.Position
import com.google.android.gms.location.LocationRequest
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class FusedPositionSource(private val context: Context) : PositionSource {

    override fun positionUpdates(locationRequest: LocationRequest): Flowable<Position> {
        return Flowable.create(FusedLocationOnSubscribe(context, locationRequest), BackpressureStrategy.LATEST)
                .map { Position(it.latitude, it.longitude) }
    }
}
