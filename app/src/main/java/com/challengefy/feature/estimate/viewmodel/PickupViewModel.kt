package com.challengefy.feature.estimate.viewmodel

import android.content.Intent
import android.databinding.ObservableField
import com.challengefy.base.di.scope.FragmentScope
import com.challengefy.base.scheduler.SchedulerManager
import com.challengefy.data.model.Address
import com.challengefy.data.repository.PlaceRepository
import com.challengefy.data.repository.PositionRepository
import com.challengefy.data.repository.PositionRepository.LocationState.ACTIVE
import com.challengefy.data.repository.PositionRepository.LocationState.NO_PERMISSION
import com.challengefy.feature.address.activity.AddressSearchActivity
import com.challengefy.feature.estimate.navigator.HomeNavigator
import com.google.android.gms.common.api.ResolvableApiException
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@FragmentScope
class PickupViewModel @Inject constructor(
        private val homeViewModel: HomeViewModel,
        private val positionRepository: PositionRepository,
        private val placeRepository: PlaceRepository,
        private val homeNavigator: HomeNavigator,
        private val schedulerManager: SchedulerManager
) : HomeNavigator.ResolutionListener {

    val viewState: ObservableField<ViewState> = ObservableField(ViewState.IDLE)
    val pickupAddress: ObservableField<Address> = ObservableField()

    private val disposables = CompositeDisposable()

    fun init() {
        homeNavigator.attachResultListener(this)

        if (pickupAddress.get() == null) {
            initLocationState(false)
        } else {
            viewState.notifyChange()
        }
    }

    fun dispose() {
        homeNavigator.detachResultListener(this)

        disposables.clear()
    }

    fun onActivateLocationClick() {
        val state = viewState.get()
        if (state == ViewState.LOCATION_NO_PERMISSION) {
            homeNavigator.requestLocationPermission(true)
        } else if (state == ViewState.LOCATION_DISABLED) {
            homeNavigator.goToLocationSettings()
        }
    }

    fun onPickUpClick() {
        homeNavigator.goToAddressSearch(pickupAddress.get())
    }

    override fun onPermissionResult(requestCode: Int, granted: Boolean) {
        if (granted) {
            locationEnabled()
        } else {
            locationNoPermission(true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            HomeNavigator.REQUEST_CODE_ADDRESS_SEARCH -> {
                val address = data?.getParcelableExtra<Address>(AddressSearchActivity.RESULT_ADDRESS)
                if (address != null) {
                    locationReceived(address)
                }
            }
            HomeNavigator.REQUEST_CODE_APP_SETTINGS,
            HomeNavigator.REQUEST_CODE_LOCATION_SETTINGS,
            HomeNavigator.REQUEST_CODE_RESOLUTION -> initLocationState(true)
        }
    }

    private fun initLocationState(alreadyRequested: Boolean) {
        positionRepository.getLocationState()
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
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(schedulerManager.mainThread())
                .subscribe(
                        {
                            locationReceived(it)
                        },
                        {
                            if (it is ResolvableApiException) {
                                homeNavigator.startResolution(it)
                            } else {

                            }
                        }
                )
                .apply { disposables.add(this) }
    }

    private fun locationReceived(address: Address) {
        pickupAddress.set(address)
        viewState.set(ViewState.LOCATION_RECEIVED)

        homeViewModel.pickUpReceived(address)
    }

    private fun locationNoPermission(alreadyRequested: Boolean) {
        if (alreadyRequested) {
            viewState.set(ViewState.LOCATION_NO_PERMISSION)
        } else {
            val displayedToUser = homeNavigator.requestLocationPermission(false)
            if (!displayedToUser) {
                viewState.set(ViewState.LOCATION_NO_PERMISSION)
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
