package com.challengefy.data.model

import com.google.gson.annotations.SerializedName

/*
[
  {
    "vehicle_type": {
      "_id": "executive_id",
      "name": "Executive Class",
      "short_name": "Executive",
      "description": "A very large vehicle with comfortable seats",
      "icons": {
        "regular": "https://cabify.com/images/icons/vehicle_type/executive_27.png"
      },
      "icon": "executive",
      "service_type": "standard"
    },
    "total_price": 1234,
    "price_formatted": "12.34€",
    "currency": "EUR",
    "currency_symbol": "€",
    "eta": {
      "min": 100,
      "max": 1000,
      "formatted": ">2 min"
    }
  }
]
 */
data class Estimate(
        @SerializedName("vehicle_type") val vehicle: Vehicle,
        @SerializedName("price_formatted") val price: String
)

data class ETA(
        @SerializedName("formatted") val time: String
)

data class Vehicle(
        @SerializedName("_id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("icons") val icons: Icons,
        @SerializedName("short_name") val shortName: String,
        @SerializedName("description") val description: String,
        @SerializedName("service_type") val serviceType: String,
        @SerializedName("eta") val eta: ETA
)

data class Icons(@SerializedName("regular") val regular: String)
