package com.challengefy.feature.map.fragment

import android.annotation.SuppressLint
import android.content.Context
import com.challengefy.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions

class MapFragment : SupportMapFragment() {

    companion object {
        private const val DEFAULT_ZOOM = 18f

        fun newInstance() = MapFragment()
    }

    private var mapState = MapState.IDLE

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getMapAsync {
            val style = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
            it.setMapStyle(style)
            it.uiSettings.setAllGesturesEnabled(true)
            it.uiSettings.isZoomControlsEnabled = true
            it.uiSettings.isCompassEnabled = false
        }
    }

    fun centerMap(latitude: Double, longitude: Double) {
        getMapAsync {
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), DEFAULT_ZOOM)

            if (mapState == MapState.IDLE) {
                it.moveCamera(cameraUpdate)
                mapState = MapState.CENTERED
            } else {
                it.animateCamera(cameraUpdate)
            }
        }
    }

    private enum class MapState {
        IDLE, CENTERED
    }

    @SuppressLint("MissingPermission")
    fun enableCurrentLocation() {
        getMapAsync {
            it.isMyLocationEnabled = true
            it.uiSettings.isMyLocationButtonEnabled = false
        }
    }
}
