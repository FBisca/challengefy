package com.challengefy.feature.ride.viewmodel

import com.challengefy.base.di.scope.FragmentScope
import com.challengefy.feature.ride.bindings.DestinationAware
import com.challengefy.feature.ride.bindings.PickupAware
import com.challengefy.feature.ride.navigator.HomeNavigator
import javax.inject.Inject

@FragmentScope
class DestinationViewModel @Inject constructor(
        override val homeNavigator: HomeNavigator,
        private val homeViewModel: HomeViewModel
) : PickupAware, DestinationAware {

    override val pickUpAddress = homeViewModel.pickUpAddress
    override val destinationAddress = homeViewModel.destinationAddress

    fun init() {
        homeNavigator.attachPickUpListener(this)
        homeNavigator.attachDestinationListener(this)
    }

    fun dispose() {
        homeNavigator.detachPickUpListener(this)
        homeNavigator.detachDestinationListener(this)
    }

    fun onConfirmClick() {
        homeViewModel.destinationReceived()
    }

}
