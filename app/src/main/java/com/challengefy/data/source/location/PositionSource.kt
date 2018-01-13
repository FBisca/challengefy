package com.challengefy.data.source.location

import com.challengefy.data.model.Position
import com.google.android.gms.location.LocationRequest
import io.reactivex.Flowable

interface PositionSource {
    fun positionUpdates(locationRequest: LocationRequest): Flowable<Position>
}
