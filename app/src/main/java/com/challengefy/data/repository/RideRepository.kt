package com.challengefy.data.repository

import com.challengefy.data.model.Address
import com.challengefy.data.model.Estimate
import io.reactivex.Single

interface RideRepository {
    fun estimateRide(start: Address, end: Address): Single<List<Estimate>>
}
