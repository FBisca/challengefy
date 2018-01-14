package com.challengefy.data.model

data class Address(
        val title: String,
        val description: String,
        val position: Position
)

data class PredictionAddress(
        val placeId: String,
        val title: String,
        val description: String
)
