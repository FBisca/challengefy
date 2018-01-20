package com.challengefy.data.repository

import io.reactivex.Single

interface LocationRepository {
    fun isLocationEnabled(): Single<Boolean>
    fun getLocationState(): Single<LocationState>
    fun isPermissionGranted(): Single<Boolean>

    enum class LocationState {
        ACTIVE, DISABLED, NO_PERMISSION
    }
}
