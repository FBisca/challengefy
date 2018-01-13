package com.challengefy.data.source.location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.location.Location
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe

class FusedLocationOnSubscribe(
        private val context: Context,
        private val locationRequest: LocationRequest
) : FlowableOnSubscribe<Location> {

    override fun subscribe(emitter: FlowableEmitter<Location>) {
        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_DENIED) {
            emitter.onError(LocationPermissionException())
        } else {
            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.locations.forEach { emitter.onNext(it) }
                }
            }

            val locationServices = LocationServices.getFusedLocationProviderClient(context)

            locationServices.lastLocation.addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    emitter.onNext(it.result)
                }

                locationServices.requestLocationUpdates(locationRequest, callback, null)
                emitter.setCancellable { locationServices.removeLocationUpdates(callback) }
            }

        }
    }
}

class LocationPermissionException : Exception()
