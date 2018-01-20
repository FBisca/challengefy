package com.challengefy.test

import com.challengefy.data.model.Address
import com.challengefy.data.model.Position
import com.challengefy.data.model.PredictionAddress

object Fabricator {

    fun address(
            title: String = "Any Street",
            description: String = "Anywhere, ANY - 003450",
            position: Position = position()
    ) = Address(
            title,
            description,
            position
    )

    fun predictionAddress(
            title: String = "Any Street",
            description: String = "Anywhere, ANY - 003450"
    ) = PredictionAddress(
            "1",
            title,
            description
    )

    fun position(
            latitude: Double = 0.0,
            longitude: Double = 0.0
    ) = Position(latitude, longitude)
}
