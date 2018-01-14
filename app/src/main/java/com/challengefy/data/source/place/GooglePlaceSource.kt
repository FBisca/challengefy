package com.challengefy.data.source.place

import android.content.Context
import com.challengefy.data.model.Address
import com.challengefy.data.model.Position
import com.challengefy.data.model.PredictionAddress
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse
import com.google.android.gms.location.places.PlaceBufferResponse
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Single

class GooglePlaceSource(private val context: Context) : PlaceSource {

    override fun autoCompletePlaces(query: String, northeast: LatLng, southwest: LatLng): Single<List<PredictionAddress>> {
        return Single.create { emitter ->
            val bounds = LatLngBounds.Builder()
                    .include(northeast)
                    .include(southwest)
                    .build()

            val typeFilter = AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                    .build()

            val client = Places.getGeoDataClient(context, null)
            client.getAutocompletePredictions(query, bounds, typeFilter).addOnCompleteListener {
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
                    emitter.onError(it.exception ?: IllegalArgumentException("Failed to get predictions"))
                }
            }
        }
    }

    private fun parsePlaceResult(predictionAddress: PredictionAddress, result: PlaceBufferResponse): Address {
        return result
                .first { it.isDataValid }
                .let {
                    Address(
                            predictionAddress.title,
                            predictionAddress.description,
                            Position(it.latLng.latitude, it.latLng.longitude, false)
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
