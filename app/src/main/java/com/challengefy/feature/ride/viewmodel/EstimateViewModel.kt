package com.challengefy.feature.ride.viewmodel

import android.databinding.Observable
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.challengefy.base.scheduler.SchedulerManager
import com.challengefy.data.model.Estimate
import com.challengefy.data.repository.RideRepository
import com.challengefy.feature.ride.bindings.DestinationAware
import com.challengefy.feature.ride.bindings.PickupAware
import com.challengefy.feature.ride.navigator.HomeNavigator
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EstimateViewModel @Inject constructor(
        override val homeNavigator: HomeNavigator,
        private val homeViewModel: HomeViewModel,
        private val rideRepository: RideRepository,
        private val schedulerManager: SchedulerManager
) : PickupAware, DestinationAware {

    override val pickUpAddress = homeViewModel.pickUpAddress
    override val destinationAddress = homeViewModel.destinationAddress
    val estimate = homeViewModel.estimateSelected
    val estimates = homeViewModel.estimates

    val itemSelected = ObservableInt()
    val viewState = ObservableField<ViewState>(ViewState.IDLE)

    private val disposables = CompositeDisposable()
    private val estimatesChangeListener = EstimatesChangeListener()

    fun init() {
        homeNavigator.attachPickUpListener(this)
        homeNavigator.attachDestinationListener(this)

        if (viewState.get() == ViewState.ESTIMATE_LOADED) {
            viewState.notifyChange()
        }

        estimates.addOnPropertyChangedCallback(estimatesChangeListener)
        triggerEstimateChange()
    }

    fun dispose() {
        homeNavigator.detachPickUpListener(this)
        homeNavigator.detachDestinationListener(this)

        estimates.removeOnPropertyChangedCallback(estimatesChangeListener)
        disposables.clear()
    }

    fun itemSelected(selectedPos: Int) {
        itemSelected.set(selectedPos)
    }

    fun onConfirmClick() {
        val estimateList = estimates.get()
        val selectedPos = itemSelected.get()

        if (estimateList != null && estimateList.size > selectedPos) {
            estimate.set(estimateList[selectedPos])
            homeViewModel.estimatedReceived()
        }
    }

    fun onTryAgainClick() {
        requestEstimates()
    }

    private fun triggerEstimateChange() {
        estimatesChangeListener.onPropertyChanged(estimates, 0)
    }

    private fun requestEstimates() {
        rideRepository.estimateRide(pickUpAddress.get(), destinationAddress.get())
                .onErrorResumeNext { createDelayedError(it) }
                .subscribeOn(schedulerManager.ioThread())
                .observeOn(schedulerManager.mainThread())
                .doOnSubscribe { viewState.set(ViewState.LOADING) }
                .subscribe(
                        {
                            estimatesLoaded(it)
                        },
                        {
                            Timber.d(it)
                            viewState.set(ViewState.ERROR)
                        }
                )
                .apply { disposables.add(this) }
    }

    private fun estimatesLoaded(result: List<Estimate>) {
        estimates.set(result)
    }

    private fun createDelayedError(error: Throwable) = Single.just(1)
            .delay(1000, TimeUnit.MILLISECONDS)
            .flatMap { Single.error<List<Estimate>>(error) }

    enum class ViewState {
        IDLE, LOADING, ESTIMATE_LOADED, ERROR
    }

    inner class EstimatesChangeListener : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable, propertyId: Int) {
            if (estimates.get() == null) {
                requestEstimates()
            } else {
                viewState.set(ViewState.ESTIMATE_LOADED)
            }
        }
    }
}
