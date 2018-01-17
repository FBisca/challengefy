package com.challengefy.feature.estimate.viewmodel

import com.challengefy.base.di.scope.FragmentScope
import javax.inject.Inject

@FragmentScope
class ConfirmPickupViewModel @Inject constructor(
        private val homeViewModel: HomeViewModel
) {

    val pickUp = homeViewModel.pickupAddress

    fun onConfirmClick() {
        homeViewModel.pickUpConfirmed()
    }
}