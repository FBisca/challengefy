package com.challengefy.feature.estimate.viewmodel

import android.databinding.ObservableField
import com.challengefy.base.scheduler.SchedulerManager
import com.challengefy.data.model.Address
import com.challengefy.data.model.Estimate
import com.challengefy.data.repository.RideRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

class EstimateViewModel @Inject constructor(
        @Named("pickup") pickup: Address,
        @Named("destination") destination: Address,
        private val homeViewModel: HomeViewModel,
        private val rideRepository: RideRepository,
        private val schedulerManager: SchedulerManager
) {

    val pickup = ObservableField<Address>(pickup)
    val destination = ObservableField<Address>(destination)
    val estimates = ObservableField<List<Estimate>>(emptyList())
    val itemSelected = ObservableField<Int>()
    val viewState = ObservableField<ViewState>(ViewState.IDLE)

    private val disposables = CompositeDisposable()

    fun init() {
        requestEstimates()
    }

    fun dispose() {
        disposables.clear()
    }

    private fun requestEstimates() {
        rideRepository.estimateRide(pickup.get(), destination.get())
                .doOnSubscribe { viewState.set(ViewState.LOADING) }
                .subscribeOn(schedulerManager.ioThread())
                .observeOn(schedulerManager.mainThread())
                .subscribe(
                        {
                            estimatesLoaded(it)
                        },
                        {

                        }
                )
                .apply { disposables.add(this) }
    }

    private fun estimatesLoaded(it: List<Estimate>?) {
        viewState.set(ViewState.ESTIMATE_LOADED)
        estimates.set(it)
    }

    fun itemSelected(selectedPos: Int) {
        itemSelected.set(selectedPos)
    }

    fun onConfirmClick() {
        val estimateList = estimates.get()
        homeViewModel.estimatedReceived(estimateList[itemSelected.get()])
    }

    enum class ViewState {
        IDLE, LOADING, ESTIMATE_LOADED
    }

}
