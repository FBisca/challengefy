package com.challengefy.map.fragment

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.SupportMapFragment

class MapFragment : SupportMapFragment() {

    companion object {
        private const val EXTRA_MAP_OPTIONS = "MapOptions"

        fun newInstance() = MapFragment()

        fun newInstance(options: GoogleMapOptions) = MapFragment().apply {
            arguments = Bundle().apply {
                putParcelable(EXTRA_MAP_OPTIONS, options)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMapAsync {
            it.uiSettings.isMyLocationButtonEnabled = true
            it.uiSettings.setAllGesturesEnabled(true)
        }
    }
}
