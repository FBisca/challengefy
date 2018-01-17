package com.challengefy.feature.estimate.viewmodel

import android.databinding.Observable
import android.databinding.ObservableField
import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.data.model.Address
import com.challengefy.data.model.Estimate
import com.challengefy.data.repository.PositionRepository
import com.challengefy.feature.estimate.navigator.HomeNavigator
import javax.inject.Inject

@ActivityScope
class HomeViewModel @Inject constructor(
        private val positionRepository: PositionRepository
) {

    val viewState: ObservableField<ViewState> = ObservableField(ViewState.PICKUP)

    val pickUpAddress = ObservableField<Address>()
    val destinationAddress = ObservableField<Address>()
    val estimateSelected = ObservableField<Estimate>()
    val estimates = ObservableField<List<Estimate>>()

    private val addressChangeListener = AddressChangeListener()

    fun init() {
        pickUpAddress.addOnPropertyChangedCallback(addressChangeListener)
        destinationAddress.addOnPropertyChangedCallback(addressChangeListener)
    }

    fun dispose() {
        pickUpAddress.removeOnPropertyChangedCallback(addressChangeListener)
        destinationAddress.removeOnPropertyChangedCallback(addressChangeListener)
    }

    fun location() = positionRepository.getCurrentPosition()

    fun pickUpReceived() {
        updateViewState(ViewState.DESTINATION)
    }

    fun destinationReceived() {
        updateViewState(ViewState.ESTIMATE)
    }

    fun estimatedReceived() {
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

    inner class AddressChangeListener : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            estimates.set(null)
        }
    }
}

interface PickupAware : HomeNavigator.PickUpListener {
    val homeNavigator: HomeNavigator
    val pickUpAddress: ObservableField<Address>

    fun onPickUpClick() {
        homeNavigator.goToAddressSearch(pickUpAddress.get(), HomeNavigator.REQUEST_CODE_PICKUP_SEARCH)
    }

    override fun onPickUpReceived(address: Address) {
        pickUpAddress.set(address)
    }
}

interface DestinationAware : HomeNavigator.DestinationListener {
    val homeNavigator: HomeNavigator
    val destinationAddress: ObservableField<Address>

    fun onDestinationClick() {
        homeNavigator.goToAddressSearch(destinationAddress.get(), HomeNavigator.REQUEST_CODE_DESTINATION_SEARCH)
    }

    override fun onDestinationReceived(address: Address) {
        destinationAddress.set(address)
    }

}
