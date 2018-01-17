package com.challengefy.feature.address.viewmodel

import android.databinding.ObservableField
import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.base.scheduler.SchedulerManager
import com.challengefy.data.model.Address
import com.challengefy.data.model.PredictionAddress
import com.challengefy.data.repository.PlaceRepository
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ActivityScope
class AddressSearchViewModel @Inject constructor(
        private val placeRepository: PlaceRepository,
        private val schedulerManager: SchedulerManager
) {
    val predictions = ObservableField<List<PredictionAddress>>()
    val viewState = ObservableField<ViewState>(ViewState.IDLE)

    private val inputSubject = PublishSubject.create<String>()
    private val keyTappedSubject = PublishSubject.create<String>()
    private val disposables = CompositeDisposable()

    fun init() {
        listenForAddresses()
    }

    fun dispose() {
        disposables.clear()
    }

    fun detailPrediction(predictionAddress: PredictionAddress): Single<Address> {
        return placeRepository.detailPrediction(predictionAddress)
    }

    fun inputSearch(text: String) {
        inputSubject.onNext(text)
    }

    fun onKeyDoneTapped() {
        keyTappedSubject.onNext("")
    }

    private fun listenForAddresses() {
        inputSubject.debounce(300L, TimeUnit.MILLISECONDS)
                .throttleLast(300L, TimeUnit.MILLISECONDS)
                .mergeWith { keyTappedSubject }
                .observeOn(schedulerManager.mainThread())
                .doOnNext { viewState.set(ViewState.LOADING) }
                .subscribeOn(schedulerManager.ioThread())
                .flatMap {
                    placeRepository.autoComplete(it, null, null)
                            .toObservable()
                }
                .observeOn(schedulerManager.mainThread())
                .subscribe(
                        {
                            predictions.set(it)
                            viewState.set(ViewState.IDLE)
                        },
                        {
                            Timber.d(it)
                        }
                )
                .addToDisposable()
    }

    private fun Disposable.addToDisposable() {
        disposables.add(this)
    }

    enum class ViewState {
        IDLE, LOADING
    }
}
