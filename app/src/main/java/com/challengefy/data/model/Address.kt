package com.challengefy.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Address(
        val title: String,
        val description: String,
        val position: Position
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class PredictionAddress(
        val placeId: String,
        val title: String,
        val description: String
) : Parcelable
