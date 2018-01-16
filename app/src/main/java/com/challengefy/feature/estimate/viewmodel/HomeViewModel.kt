package com.challengefy.feature.estimate.viewmodel

import android.databinding.ObservableField
import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.data.model.Address
import com.challengefy.data.repository.PositionRepository
import javax.inject.Inject

@ActivityScope
class HomeViewModel @Inject constructor(
        private val positionRepository: PositionRepository
) {

    val viewState: ObservableField<ViewState> = ObservableField(ViewState.PICKUP)

    val pickupAddress: ObservableField<Address> = ObservableField()
    val destinationAddress: ObservableField<Address> = ObservableField()

    fun location() = positionRepository.getCurrentPosition()

    fun pickUpReceived(address: Address) {
        pickupAddress.set(address)
        viewState.set(ViewState.DESTINATION)
    }

    enum class ViewState {
        PICKUP, DESTINATION
    }
}
