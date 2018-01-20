package com.challengefy.feature.ride.viewmodel

import android.databinding.ObservableField
import com.challengefy.base.di.scope.FragmentScope
import com.challengefy.base.scheduler.SchedulerManager
import com.challengefy.base.util.distanceTo
import com.challengefy.data.model.Address
import com.challengefy.data.repository.LocationRepository
import com.challengefy.data.repository.LocationRepository.LocationState.ACTIVE
import com.challengefy.data.repository.LocationRepository.LocationState.NO_PERMISSION
import com.challengefy.data.repository.PlaceRepository
import com.challengefy.feature.ride.bindings.PickupAware
import com.challengefy.feature.ride.navigator.HomeNavigator
import com.google.android.gms.common.api.ResolvableApiException
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@FragmentScope
class PickupViewModel @Inject constructor(
        override val homeNavigator: HomeNavigator,
        private val homeViewModel: HomeViewModel,
        private val locationRepository: LocationRepository,
        private val placeRepository: PlaceRepository,
        private val schedulerManager: SchedulerManager
) : HomeNavigator.ResolutionListener, PickupAware {

    companion object {
        const val MAXIMUM_PLACE_DISTANCE_METERS = 30.0
    }

    override val pickUpAddress = homeViewModel.pickUpAddress

    val viewState: ObservableField<ViewState> = ObservableField(ViewState.IDLE)

    private val disposables = CompositeDisposable()

    fun init() {
        homeNavigator.attachResultListener(this)
        homeNavigator.attachPickUpListener(this)

        if (pickUpAddress.get() == null) {
            initLocationState(false)
        } else {
            viewState.notifyChange()
        }
    }

    fun dispose() {
        homeNavigator.detachResultListener(this)
        homeNavigator.detachPickUpListener(this)

        disposables.clear()
    }

    fun onActivateLocationClick() {
        val state = viewState.get()
        if (state == ViewState.LOCATION_NO_PERMISSION) {
            if (homeNavigator.shouldShowRationaleLocation()) {
                homeNavigator.requestLocationPermission()
            } else {
                homeNavigator.goToAppSettings()
            }
        } else if (state == ViewState.LOCATION_DISABLED) {
            homeNavigator.goToLocationSettings()
        }
    }

    fun onConfirmClick() {
        homeViewModel.pickUpReceived()
    }

    override fun onPermissionResult(requestCode: Int, granted: Boolean) {
        if (granted) {
            locationEnabled()
        } else {
            locationNoPermission(true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int) {
        when (requestCode) {
            HomeNavigator.REQUEST_CODE_APP_SETTINGS,
            HomeNavigator.REQUEST_CODE_LOCATION_SETTINGS,
            HomeNavigator.REQUEST_CODE_RESOLUTION -> initLocationState(true)
        }
    }

    private fun initLocationState(alreadyRequested: Boolean) {
        locationRepository.getLocationState()
                .subscribe(
                        {
                            when (it) {
                                ACTIVE -> locationEnabled()
                                NO_PERMISSION -> locationNoPermission(false)
                                else -> locationDisabled()
                            }
                        },
                        {
                            if (!alreadyRequested && it is ResolvableApiException) {
                                homeNavigator.startResolution(it)
                            } else {
                                locationDisabled()
                            }
                        }
                )
                .apply { disposables.add(this) }
    }

    private fun locationEnabled() {
        placeRepository.getCurrentPlace()
                .doOnSubscribe { loading() }
                .flatMap { checkPlaceCloseEnough(it) }
                .delay(500, TimeUnit.MILLISECONDS, schedulerManager.timeScheduler())
                .observeOn(schedulerManager.mainThread())
                .subscribe(
                        {
                            locationReceived(it)
                        },
                        {
                            if (it is ResolvableApiException) {
                                homeNavigator.startResolution(it)
                            } else {
                                locationDisabled()
                            }
                        }
                )
                .apply { disposables.add(this) }
    }

    private fun checkPlaceCloseEnough(address: Address): Single<Address> {
        return locationRepository.getUserLocation()
                .flatMap {
                    val placeSingle = Single.just(address)
                    if (address.distanceTo(it) > MAXIMUM_PLACE_DISTANCE_METERS) {
                        placeRepository.getAddressByPosition(it)
                                .switchIfEmpty(placeSingle)
                                .onErrorReturnItem(address)
                    } else {
                        placeSingle
                    }
                }
    }

    private fun locationReceived(address: Address) {
        pickUpAddress.set(address)
        viewState.set(ViewState.LOCATION_RECEIVED)

        homeViewModel.pickUpReceived()
    }

    private fun locationNoPermission(alreadyRequested: Boolean) {
        if (alreadyRequested) {
            viewState.set(ViewState.LOCATION_NO_PERMISSION)
        } else {
            if (homeNavigator.shouldShowRationaleLocation()) {
                viewState.set(ViewState.LOCATION_NO_PERMISSION)
            } else {
                homeNavigator.requestLocationPermission()
            }
        }
    }

    private fun locationDisabled() {
        viewState.set(ViewState.LOCATION_DISABLED)
    }

    private fun loading() {
        viewState.set(ViewState.LOADING)
    }

    enum class ViewState {
        IDLE, LOADING, LOCATION_DISABLED, LOCATION_NO_PERMISSION, LOCATION_RECEIVED
    }

}
