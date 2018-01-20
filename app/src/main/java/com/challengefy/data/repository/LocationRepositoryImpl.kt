package com.challengefy.data.repository

import com.challengefy.base.util.OpenForTests
import com.challengefy.data.source.location.LocationSource
import com.google.android.gms.location.LocationRequest
import io.reactivex.Single
import javax.inject.Inject

@OpenForTests
class LocationRepositoryImpl @Inject constructor(
        private val locationSource: LocationSource
) : LocationRepository {

    private val locationRequest = LocationRequest().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 1000
    }

    override fun isLocationEnabled(): Single<Boolean> {
        return locationSource.isLocationEnabled(locationRequest)
    }

    override fun isPermissionGranted(): Single<Boolean> {
        return locationSource.isPermissionGranted()
    }

    override fun getLocationState(): Single<LocationRepository.LocationState> {
        return isPermissionGranted()
                .concatWith(isLocationEnabled())
                .toList()
                .map {
                    val granted = it[0]
                    val enabled = it[1]

                    when {
                        !granted -> LocationRepository.LocationState.NO_PERMISSION
                        !enabled -> LocationRepository.LocationState.DISABLED
                        else -> LocationRepository.LocationState.ACTIVE
                    }
                }
    }
}
