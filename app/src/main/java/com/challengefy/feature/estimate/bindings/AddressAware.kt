package com.challengefy.feature.estimate.bindings

import android.databinding.ObservableField
import com.challengefy.data.model.Address
import com.challengefy.feature.estimate.navigator.HomeNavigator


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