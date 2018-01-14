package com.challengefy.data.network.api

import com.challengefy.data.model.Estimate
import com.challengefy.data.network.request.EstimateRideRequest
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface EstimateApi {

    @POST("/api/v2/estimate")
    fun estimateRide(@Body body: EstimateRideRequest): Single<List<Estimate>>
}
