package com.challengefy.data.source.location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.support.v4.content.ContextCompat
import com.challengefy.data.model.Position
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject


class FusedPositionSource @Inject constructor(
        private val context: Context
) : PositionSource {

    override fun positionUpdates(locationRequest: LocationRequest): Flowable<Position> {
        return Flowable.create(FusedLocationOnSubscribe(context, locationRequest), BackpressureStrategy.LATEST)
    }

    override fun isLocationEnabled(locationRequest: LocationRequest): Single<Boolean> {
        return Single.create { emitter ->
            val client = LocationServices.getSettingsClient(context)

            val task = client.checkLocationSettings(LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
                    .build())

            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    val isEnabled = it.result.locationSettingsStates.isLocationUsable
                    emitter.onSuccess(isEnabled)
                } else {
                    emitter.onError(it.exception ?: IllegalArgumentException("Error on getting location settings"))
                }
            }
        }
    }

    override fun isPermissionGranted(): Single<Boolean> {
        return Single.just(ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED)
    }
}
