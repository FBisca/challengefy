package com.challengefy.estimate.viewmodel

import com.challengefy.data.repository.PositionRepository
import javax.inject.Inject

class EstimateViewModel @Inject constructor(
        private val positionRepository: PositionRepository
) {

    fun location() = positionRepository.getCurrentPosition()
}
