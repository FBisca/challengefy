package com.challengefy.data.repository

import com.challengefy.data.source.location.PositionSource
import com.google.android.gms.location.LocationRequest
import io.reactivex.Single
import javax.inject.Inject

class PositionRepositoryImpl @Inject constructor(
        private val positionSource: PositionSource
) : PositionRepository {

    private val locationRequest = LocationRequest().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 1000
    }

    override fun isLocationEnabled(): Single<Boolean> {
        return positionSource.isLocationEnabled(locationRequest)
    }

    override fun isPermissionGranted(): Single<Boolean> {
        return positionSource.isPermissionGranted()
    }

    override fun getLocationState(): Single<PositionRepository.LocationState> {
        return isPermissionGranted()
                .concatWith(isLocationEnabled())
                .toList()
                .map {
                    val granted = it[0]
                    val enabled = it[1]

                    when {
                        !granted -> PositionRepository.LocationState.NO_PERMISSION
                        !enabled -> PositionRepository.LocationState.DISABLED
                        else -> PositionRepository.LocationState.ACTIVE
                    }
                }
    }
}
