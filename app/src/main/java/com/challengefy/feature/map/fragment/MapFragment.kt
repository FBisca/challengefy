package com.challengefy.feature.map.fragment

import android.content.Context
import android.databinding.Observable
import android.os.Bundle
import android.util.SparseArray
import com.challengefy.R
import com.challengefy.feature.ride.bindings.MapPaddingBinding
import com.challengefy.feature.map.viewmodel.MapViewModel
import com.challengefy.feature.map.viewmodel.MapViewModel.ViewState.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
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
    lateinit var viewModel: MapViewModel

    @Inject
    lateinit var mapBinding: MapPaddingBinding

    private var paddingDisposable = Disposables.empty()

    private val viewStateChangeListener = ViewStateChangeListener()

    private val markers = SparseArray<Marker>()

    private var hasMoved = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        getMapAsync {
            val style = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
            it.setMapStyle(style)
            it.uiSettings.setAllGesturesEnabled(true)
            it.uiSettings.isCompassEnabled = false
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        viewModel.init()
        viewModel.viewState.addOnPropertyChangedCallback(viewStateChangeListener)

        bindPaddingChangeEvents()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dispose()
        viewModel.viewState.removeOnPropertyChangedCallback(viewStateChangeListener)

        paddingDisposable.dispose()
    }

    private fun bindPaddingChangeEvents() {
        paddingDisposable = mapBinding.paddingChangeEvents()
                .flatMapMaybe { padding -> getMapEmitter().map { it to padding } }
                .subscribe(
                        { (map, padding) ->
                            map.setPadding(padding.left, padding.top, padding.right, padding.bottom)
                            checkMapState()
                        },
                        {
                            Timber.e(it)
                        })
    }

    private fun showDestination() {
        viewModel.destinationAddress.get()?.let {
            centerMap(it.position.latitude, it.position.longitude)
            addMarker(it.position.latitude, it.position.longitude, R.drawable.destination_marker)
        }
    }

    private fun showPickup() {
        viewModel.pickUpAddress.get()?.let {
            centerMap(it.position.latitude, it.position.longitude)
            addMarker(it.position.latitude, it.position.longitude, R.drawable.pickup_marker)
        }
    }

    private fun addMarker(latitude: Double, longitude: Double, res: Int) {
        getMapAsync {
            val oldMarker = markers[res]
            oldMarker?.remove()

            val marker = it.addMarker(MarkerOptions()
                    .position(LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.fromResource(res))
            )

            markers.put(res, marker)
        }
    }

    private fun centerMap(latitude: Double, longitude: Double) {
        getMapAsync {
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), DEFAULT_ZOOM)

            if (hasMoved) {
                it.animateCamera(cameraUpdate)
            } else {
                it.moveCamera(cameraUpdate)
                hasMoved = true
            }
        }
    }

    private fun checkMapState() {
        val viewState = viewModel.viewState.get()
        when (viewState) {
            SHOW_DESTINATION -> showDestination()
            SHOW_PICKUP -> showPickup()
            else -> Unit
        }
    }

    private fun getMapEmitter(): Maybe<GoogleMap> {
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

    inner class ViewStateChangeListener : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            checkMapState()
        }

    }
}
