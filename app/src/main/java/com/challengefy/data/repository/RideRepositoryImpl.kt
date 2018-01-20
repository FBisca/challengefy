package com.challengefy.data.repository

import com.challengefy.base.util.OpenForTests
import com.challengefy.data.model.Address
import com.challengefy.data.model.Estimate
import com.challengefy.data.network.api.EstimateApi
import com.challengefy.data.network.request.EstimateRideRequest
import com.challengefy.data.network.request.Stop
import io.reactivex.Single
import javax.inject.Inject

@OpenForTests
class RideRepositoryImpl @Inject constructor(
        private val api: EstimateApi
) : RideRepository {

    override fun estimateRide(start: Address, end: Address): Single<List<Estimate>> {
        val stops = listOf(start, end)
                .map {
                    Stop(
                            listOf(it.position.latitude, it.position.longitude),
                            it.title,
                            it.description
                    )
                }


        return api.estimateRide(EstimateRideRequest(stops))
    }
}
