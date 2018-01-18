package com.challengefy.feature.estimate.viewmodel

import com.challengefy.base.di.scope.FragmentScope
import com.challengefy.feature.estimate.navigator.HomeNavigator
import javax.inject.Inject

@FragmentScope
class LookingForCarViewModel @Inject constructor(
        private val homeNavigator: HomeNavigator
) {

    fun onCancelClick() {
        homeNavigator.goBack()
    }
}
