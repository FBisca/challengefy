package com.challengefy.feature.estimate.viewmodel

import com.challengefy.base.scheduler.SchedulerManager
import com.challengefy.data.model.Address
import com.challengefy.data.model.Estimate
import com.challengefy.data.repository.RideRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class EstimateViewModel @Inject constructor(
        @Named("pickup") private val pickup: Address,
        @Named("destination") private val destination: Address,
        private val rideRepository: RideRepository,
        private val schedulerManager: SchedulerManager
) {

    fun estimate(): Single<List<Estimate>> {
        return rideRepository.estimateRide(pickup, destination)
                .subscribeOn(schedulerManager.ioThread())
                .observeOn(schedulerManager.mainThread())
    }

}
