package com.challengefy.data.source.location

import android.content.Context
import com.challengefy.data.model.Position
import com.google.android.gms.location.LocationRequest
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import javax.inject.Inject

class FusedPositionSource @Inject constructor(
        private val context: Context
) : PositionSource {

    override fun positionUpdates(locationRequest: LocationRequest): Flowable<Position> {
        return Flowable.create(FusedLocationOnSubscribe(context, locationRequest), BackpressureStrategy.LATEST)
    }
}
