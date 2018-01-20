package com.challengefy.data.source.location

import com.challengefy.data.model.Position
import com.google.android.gms.location.LocationRequest
import io.reactivex.Flowable
import io.reactivex.Single

interface LocationSource {
    fun positionUpdates(locationRequest: LocationRequest): Flowable<Position>
    fun isLocationEnabled(locationRequest: LocationRequest): Single<Boolean>
    fun isPermissionGranted(): Single<Boolean>
}
