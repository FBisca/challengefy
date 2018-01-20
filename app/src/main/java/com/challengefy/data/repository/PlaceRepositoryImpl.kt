package com.challengefy.data.repository

import com.challengefy.base.util.OpenForTests
import com.challengefy.data.model.Address
import com.challengefy.data.model.PredictionAddress
import com.challengefy.data.source.place.PlaceSource
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import javax.inject.Inject

@OpenForTests
class PlaceRepositoryImpl @Inject constructor(
        private val placeSource: PlaceSource
) : PlaceRepository {

    override fun autoComplete(
            query: String,
            northeast: LatLng?,
            southwest: LatLng?
    ): Single<List<PredictionAddress>> {
        return placeSource.autoCompletePlaces(query, northeast, southwest)
    }

    override fun detailPrediction(predictionAddress: PredictionAddress): Single<Address> {
        return placeSource.detailPrediction(predictionAddress)
    }

    override fun getCurrentPlace(): Single<Address> {
        return placeSource.getCurrentPlace()
    }
}
