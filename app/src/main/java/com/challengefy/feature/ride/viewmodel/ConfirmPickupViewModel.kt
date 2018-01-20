package com.challengefy.feature.ride.viewmodel

import com.challengefy.base.di.scope.FragmentScope
import com.challengefy.feature.ride.bindings.DestinationAware
import com.challengefy.feature.ride.bindings.PickupAware
import com.challengefy.feature.ride.navigator.HomeNavigator
import javax.inject.Inject

@FragmentScope
class ConfirmPickupViewModel @Inject constructor(
        override val homeNavigator: HomeNavigator,
        private val homeViewModel: HomeViewModel
) : PickupAware, DestinationAware{

    override val pickUpAddress = homeViewModel.pickUpAddress
    override val destinationAddress = homeViewModel.destinationAddress
    val estimateSelected = homeViewModel.estimateSelected

    fun onConfirmClick() {
        homeViewModel.pickUpConfirmed()
    }
}