package com.challengefy.test

import com.challengefy.data.model.*

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

    fun estimate(
            vehicle: Vehicle = vehicle(),
            price: String = "$20.00",
            eta: ETA = ETA(">1 min")
    ) = Estimate(
            vehicle,
            price,
            eta
    )

    fun vehicle(
            id: String = "1",
            name: String = "top vehicle",
            icons: Icons = Icons("iconUrl"),
            shortName: String = "tv",
            description: String = "an topper vehicle",
            vehicleType: String = "topster_vehicle"
    ) = Vehicle(
            id,
            name,
            icons,
            shortName,
            description,
            vehicleType
    )
}
