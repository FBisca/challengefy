package com.challengefy.address.viewmodel

import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.data.model.Address
import com.challengefy.data.model.PredictionAddress
import com.challengefy.data.repository.PlaceRepository
import com.challengefy.data.repository.PositionRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ActivityScope
class AddressSearchViewModel @Inject constructor(
        private val positionRepository: PositionRepository,
        private val placeRepository: PlaceRepository
) {

    private val inputSubject = PublishSubject.create<String>()

    fun addresses(): Observable<List<PredictionAddress>> {
        return inputSubject
                .debounce(300L, TimeUnit.MILLISECONDS)
                .throttleLast(300L, TimeUnit.MILLISECONDS)
                .flatMap {
                    placeRepository.autoComplete(it, null, null)
                            .toObservable()
                }
    }

    fun detailPrediction(predictionAddress: PredictionAddress): Single<Address> {
        return placeRepository.detailPrediction(predictionAddress)
    }

    fun inputSearch(text: String) {
        inputSubject.onNext(text)
    }

}
