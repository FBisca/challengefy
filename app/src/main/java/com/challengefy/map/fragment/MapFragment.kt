package com.challengefy.map.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapFragment : SupportMapFragment() {

    companion object {
        private const val EXTRA_MAP_OPTIONS = "MapOptions"
        private const val DEFAULT_ZOOM = 18f

        fun newInstance() = MapFragment()

        fun newInstance(options: GoogleMapOptions) = MapFragment().apply {
            arguments = Bundle().apply {
                putParcelable(EXTRA_MAP_OPTIONS, options)
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getMapAsync {
            it.uiSettings.isMyLocationButtonEnabled = true
            it.uiSettings.setAllGesturesEnabled(true)
        }
    }

    fun activateMyLocation() {
        context?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getMapAsync { it.isMyLocationEnabled = true }
            }
        }
    }

    fun centerMap(latitude: Double, longitude: Double) {
        getMapAsync { it.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), DEFAULT_ZOOM)) }
    }
}
