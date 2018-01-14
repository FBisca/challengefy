package com.challengefy.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Position(
        val latitude: Double,
        val longitude: Double,
        val lastLocation: Boolean
) : Parcelable
