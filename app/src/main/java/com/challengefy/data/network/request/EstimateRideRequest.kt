package com.challengefy.data.network.request

import com.google.gson.annotations.SerializedName

data class EstimateRideRequest(
        val stops: List<Stop>
)

data class Stop(
        @SerializedName("loc") val position: List<Double>,
        @SerializedName("name") val name: String,
        @SerializedName("num") val number: String,
        @SerializedName("addr") val address: String,
        @SerializedName("city") val city: String,
        @SerializedName("country") val country: String
)
