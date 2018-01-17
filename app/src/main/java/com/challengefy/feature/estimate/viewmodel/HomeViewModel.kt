package com.challengefy.feature.estimate.viewmodel

import android.databinding.ObservableField
import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.data.model.Address
import com.challengefy.data.model.Estimate
import com.challengefy.data.repository.PositionRepository
import javax.inject.Inject

@ActivityScope
class HomeViewModel @Inject constructor(
        private val positionRepository: PositionRepository
) {

    val viewState: ObservableField<ViewState> = ObservableField(ViewState.PICKUP)

    val pickupAddress: ObservableField<Address> = ObservableField()
    val destinationAddress: ObservableField<Address> = ObservableField()
    val estimate: ObservableField<Estimate> = ObservableField()

    fun location() = positionRepository.getCurrentPosition()

    fun pickUpReceived(address: Address) {
        this.pickupAddress.set(address)
        updateViewState(ViewState.DESTINATION)
    }

    fun destinationReceived(address: Address) {
        this.destinationAddress.set(address)
        updateViewState(ViewState.ESTIMATE)
    }

    fun estimatedReceived(estimate: Estimate) {
        this.estimate.set(estimate)
        updateViewState(ViewState.CONFIRM_PICKUP)
    }

    fun pickUpConfirmed() {

    }

    private fun updateViewState(newState: ViewState) {
        if (newState != viewState.get()) {
            viewState.set(newState)
        } else {
            viewState.notifyChange()
        }
    }

    enum class ViewState {
        PICKUP, DESTINATION, ESTIMATE, CONFIRM_PICKUP, FINDING_CAR
    }
}
