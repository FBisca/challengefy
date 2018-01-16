package com.challengefy.data.repository

import com.challengefy.data.model.Position
import io.reactivex.Single

interface PositionRepository {
    fun getCurrentPosition(): Single<Position>
    fun isLocationEnabled(): Single<Boolean>
    fun getLocationState(): Single<LocationState>
    fun isPermissionGranted(): Single<Boolean>

    enum class LocationState {
        ACTIVE, DISABLED, NO_PERMISSION
    }
}
