package com.challengefy.data.source.place

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.support.v4.content.ContextCompat
import com.challengefy.base.util.OpenForTests
import com.challengefy.data.model.Address
import com.challengefy.data.model.Position
import com.challengefy.data.model.PredictionAddress
import com.google.android.gms.location.places.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Single
import javax.inject.Inject

@OpenForTests
class GooglePlaceSource @Inject constructor(
        private val context: Context
) : PlaceSource {

    override fun autoCompletePlaces(query: String, northeast: LatLng?, southwest: LatLng?): Single<List<PredictionAddress>> {
        return Single.create { emitter ->
            val bounds = when {
                northeast != null && southwest != null -> LatLngBounds.Builder()
                        .include(northeast)
                        .include(southwest)
                        .build()
                else -> null
            }

            val client = Places.getGeoDataClient(context, null)
            client.getAutocompletePredictions(query, bounds, null).addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onSuccess(parsePredictionResult(it.result))
                } else {
                    emitter.onError(it.exception ?: IllegalArgumentException("Failed to get predictions"))
                }
            }
        }
    }

    override fun detailPrediction(predictionAddress: PredictionAddress): Single<Address> {
        return Single.create { emitter ->
            val client = Places.getGeoDataClient(context, null)
            client.getPlaceById(predictionAddress.placeId).addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onSuccess(parsePlaceResult(predictionAddress, it.result))
                } else {
                    emitter.onError(it.exception ?: IllegalArgumentException("Failed to detail prediction"))
                }
            }
        }
    }

    override fun getCurrentPlace(): Single<Address> {
        return Single.create { emitter ->
            if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
                emitter.onError(IllegalArgumentException("Permission Required"))
            } else {
                val client = Places.getPlaceDetectionClient(context, null)
                client.getCurrentPlace(null).addOnCompleteListener {
                    if (it.isSuccessful) {
                        emitter.onSuccess(parseCurrentPlace(it.result))
                    } else {
                        emitter.onError(it.exception ?: IllegalArgumentException("Failed to get current place"))
                    }
                }
            }
        }
    }

    private fun parseCurrentPlace(result: PlaceLikelihoodBufferResponse): Address {
        val place = result.sortedByDescending { it.likelihood }
                .first()
                .place
        return Address(
                place.name.toString(),
                place.address.toString(),
                Position(place.latLng.latitude, place.latLng.longitude)
        )
    }

    private fun parsePlaceResult(predictionAddress: PredictionAddress, result: PlaceBufferResponse): Address {
        return result
                .first { it.isDataValid }
                .let {
                    Address(
                            predictionAddress.title,
                            predictionAddress.description,
                            Position(it.latLng.latitude, it.latLng.longitude)
                    )
                }
    }


    private fun parsePredictionResult(result: AutocompletePredictionBufferResponse): List<PredictionAddress> {
        return result
                .filter { it.placeId != null }
                .map {
                    PredictionAddress(
                            it.placeId.toString(),
                            it.getPrimaryText(null).toString(),
                            it.getSecondaryText(null).toString()
                    )
                }
    }

}
