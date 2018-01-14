package com.challengefy.estimate.viewmodel

import com.challengefy.data.repository.PlaceRepository
import com.challengefy.data.repository.PositionRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
        private val positionRepository: PositionRepository,
        private val placeRepository: PlaceRepository
) {

    fun location() = positionRepository.getCurrentPosition()
    fun currentLocation() = placeRepository.getCurrentPlace()
}
