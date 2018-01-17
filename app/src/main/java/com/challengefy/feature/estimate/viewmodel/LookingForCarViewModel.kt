package com.challengefy.feature.estimate.viewmodel

import com.challengefy.base.di.scope.FragmentScope
import javax.inject.Inject

@FragmentScope
class LookingForCarViewModel @Inject constructor(
        private val homeViewModel: HomeViewModel
) {

    fun onCancelClick() {
    }
}