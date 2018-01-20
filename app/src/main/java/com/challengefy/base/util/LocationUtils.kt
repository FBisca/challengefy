package com.challengefy.base.util

import com.challengefy.data.model.Address
import com.challengefy.data.model.Position

fun distance(lat1: Double, lat2: Double, lon1: Double,
             lon2: Double, el1: Double, el2: Double): Double {

    val R = 6371 // Radius of the earth

    val latDistance = Math.toRadians(lat2 - lat1)
    val lonDistance = Math.toRadians(lon2 - lon1)
    val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + (Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2))
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    var distance = R.toDouble() * c * 1000.0 // convert to meters

    val height = el1 - el2

    distance = Math.pow(distance, 2.0) + Math.pow(height, 2.0)

    return Math.sqrt(distance)
}


fun Address.distanceTo(position: Position): Double {
    return distance(
            this.position.latitude,
            this.position.longitude,
            position.latitude,
            position.longitude,
            0.0,
            0.0)
}
