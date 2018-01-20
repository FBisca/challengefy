package com.challengefy.feature.ride.viewmodel

import com.challengefy.base.di.scope.FragmentScope
import com.challengefy.feature.ride.navigator.HomeNavigator
import javax.inject.Inject

@FragmentScope
class LookingForCarViewModel @Inject constructor(
        private val homeNavigator: HomeNavigator
) {

    fun onCancelClick() {
        homeNavigator.goBack()
    }
}
