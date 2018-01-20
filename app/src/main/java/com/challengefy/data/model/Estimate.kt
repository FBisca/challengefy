

package com.challengefy.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Estimate(
        @SerializedName("vehicle_type") val vehicle: Vehicle,
        @SerializedName("price_formatted") val price: String
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ETA(
        @SerializedName("formatted") val time: String
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Vehicle(
        @SerializedName("_id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("icons") val icons: Icons,
        @SerializedName("short_name") val shortName: String,
        @SerializedName("description") val description: String?,
        @SerializedName("service_type") val serviceType: String?,
        @SerializedName("eta") val eta: ETA?
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Icons(
        @SerializedName("regular") val regular: String
) : Parcelable
