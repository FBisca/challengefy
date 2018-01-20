package com.challengefy.data.repository

import com.challengefy.data.model.Position
import io.reactivex.Single

interface LocationRepository {
    fun getUserLocation(): Single<Position>
    fun getLocationState(): Single<LocationState>
    fun isLocationEnabled(): Single<Boolean>
    fun isPermissionGranted(): Single<Boolean>

    enum class LocationState {
        ACTIVE, DISABLED, NO_PERMISSION
    }
}
