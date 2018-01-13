package com.challengefy.data.repository

import com.challengefy.data.model.Position
import com.challengefy.data.source.location.PositionSource
import com.google.android.gms.location.LocationRequest
import io.reactivex.Single
import javax.inject.Inject

class PositionRepositoryImpl @Inject constructor(
        private val positionSource: PositionSource
) : PositionRepository {

    override fun getCurrentPosition(): Single<Position> {
        val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }

        return positionSource.positionUpdates(locationRequest)
                .firstOrError()
    }
}
