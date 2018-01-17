package com.challengefy.feature.map.viewmodel

import com.challengefy.base.di.scope.FragmentScope
import com.challengefy.feature.estimate.viewmodel.HomeViewModel
import javax.inject.Inject

@FragmentScope
class MapViewModel @Inject constructor(
        private val homeViewModel: HomeViewModel
) {
}