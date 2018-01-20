package com.challengefy.data.source.location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.location.Location
import android.support.v4.content.ContextCompat
import com.challengefy.data.model.Position
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe

class FusedLocationOnSubscribe(
        private val context: Context,
        private val locationRequest: LocationRequest
) : FlowableOnSubscribe<Position> {

    override fun subscribe(emitter: FlowableEmitter<Position>) {
        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_DENIED) {
            emitter.onError(LocationPermissionException())
        } else {
            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.locations.forEach {
                        emitter.onNext(createPositionFromLocation(it))
                    }
                }
            }

            val locationServices = LocationServices.getFusedLocationProviderClient(context)

            locationServices.lastLocation.addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    emitter.onNext(createPositionFromLocation(it.result))
                }

                locationServices.requestLocationUpdates(locationRequest, callback, null)
                emitter.setCancellable {
                    locationServices.removeLocationUpdates(callback)
                }
            }

        }
    }

    private fun createPositionFromLocation(location: Location) = Position(
            location.latitude,
            location.longitude
    )
}

class LocationPermissionException : Exception()
