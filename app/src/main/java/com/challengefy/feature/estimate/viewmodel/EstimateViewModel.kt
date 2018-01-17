package com.challengefy.feature.estimate.viewmodel

import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.challengefy.base.scheduler.SchedulerManager
import com.challengefy.data.model.Estimate
import com.challengefy.data.repository.RideRepository
import com.challengefy.feature.estimate.navigator.HomeNavigator
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
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

    fun init() {
        homeNavigator.attachPickUpListener(this)
        homeNavigator.attachDestinationListener(this)

        if (estimates.get() == null) {
            requestEstimates()
        }
    }

    fun dispose() {
        homeNavigator.detachPickUpListener(this)
        homeNavigator.detachDestinationListener(this)

        disposables.clear()
    }

    private fun requestEstimates() {
        rideRepository.estimateRide(pickUpAddress.get(), destinationAddress.get())
                .doOnSubscribe { viewState.set(ViewState.LOADING) }
                .subscribeOn(schedulerManager.ioThread())
                .observeOn(schedulerManager.mainThread())
                .subscribe(
                        {
                            estimatesLoaded(it)
                        },
                        {
                            Timber.d(it)
                        }
                )
                .apply { disposables.add(this) }
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

    private fun estimatesLoaded(it: List<Estimate>?) {
        estimates.set(it)
        viewState.set(ViewState.ESTIMATE_LOADED)
    }

    enum class ViewState {
        IDLE, LOADING, ESTIMATE_LOADED
    }

}
