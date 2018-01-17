package com.challengefy.feature.map.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.challengefy.R
import com.challengefy.feature.estimate.bindings.MapPaddingBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Maybe
import io.reactivex.disposables.Disposables
import timber.log.Timber
import javax.inject.Inject

class MapFragment : SupportMapFragment() {

    companion object {
        private const val DEFAULT_ZOOM = 18f

        fun newInstance() = MapFragment()
    }

    @Inject
    lateinit var mapBinding: MapPaddingBinding

    private var mapState = MapState.IDLE

    private var paddingDisposable = Disposables.empty()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        getMapAsync {
            val style = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
            it.setMapStyle(style)
            it.uiSettings.setAllGesturesEnabled(true)
            it.uiSettings.isZoomControlsEnabled = true
            it.uiSettings.isCompassEnabled = false
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        bindPaddingChangeEvents()
    }

    override fun onDestroy() {
        super.onDestroy()
        paddingDisposable.dispose()
    }

    private fun bindPaddingChangeEvents() {
        paddingDisposable = mapBinding.paddingChangeEvents()
                .flatMapMaybe { padding -> getMap().map { it to padding } }
                .subscribe(
                        { (map, padding) ->
                            map.setPadding(padding.left, padding.top, padding.right, padding.bottom)
                        },
                        {
                            Timber.e(it)
                        })
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

    private fun getMap(): Maybe<GoogleMap> {
        return Maybe.create { emitter ->
            getMapAsync {
                if (it != null) {
                    emitter.onSuccess(it)
                } else {
                    emitter.onComplete()
                }
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
