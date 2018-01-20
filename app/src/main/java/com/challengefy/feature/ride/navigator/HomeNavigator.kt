package com.challengefy.feature.ride.navigator

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.data.model.Address
import com.challengefy.feature.address.activity.AddressSearchActivity
import com.challengefy.feature.ride.activity.HomeActivity
import com.google.android.gms.common.api.ResolvableApiException
import javax.inject.Inject


@ActivityScope
class HomeNavigator @Inject constructor(
        private val activity: HomeActivity
) {
    companion object {
        const val REQUEST_CODE_PERMISSION = 1
        const val REQUEST_CODE_RESOLUTION = 2
        const val REQUEST_CODE_LOCATION_SETTINGS = 3
        const val REQUEST_CODE_APP_SETTINGS = 4
        const val REQUEST_CODE_PICKUP_SEARCH = 5
        const val REQUEST_CODE_DESTINATION_SEARCH = 6
    }

    private val resolutionListeners = mutableSetOf<ResolutionListener>()
    private val pickUpListeners = mutableSetOf<PickUpListener>()
    private val destinationListeners = mutableSetOf<DestinationListener>()

    fun requestLocationPermission(isUserAction: Boolean): Boolean {
        return when {
            shouldShowRationaleLocation() -> {
                ActivityCompat.requestPermissions(activity, arrayOf(ACCESS_FINE_LOCATION), REQUEST_CODE_PERMISSION)
                true
            }
            isUserAction -> {
                goToAppSettings()
                true
            }
            else -> false
        }
    }

    fun startResolution(exception: ResolvableApiException) {
        exception.startResolutionForResult(activity, REQUEST_CODE_RESOLUTION)
    }

    fun goToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.packageName, null))
        activity.startActivityForResult(intent, REQUEST_CODE_APP_SETTINGS)
    }

    fun goToLocationSettings() {
        activity.startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CODE_LOCATION_SETTINGS)
    }

    fun goToAddressSearch(currentAddress: Address?, requestCode: Int) {
        val intent = AddressSearchActivity.startIntent(activity, currentAddress)
        activity.startActivityForResult(intent, requestCode)
    }

    fun goBack() {
        activity.onBackPressed()
    }

    fun attachResultListener(listener: ResolutionListener) {
        resolutionListeners.add(listener)
    }

    fun detachResultListener(listener: ResolutionListener) {
        resolutionListeners.remove(listener)
    }

    fun attachPickUpListener(listener: PickUpListener) {
        pickUpListeners.add(listener)
    }

    fun detachPickUpListener(listener: PickUpListener) {
        pickUpListeners.remove(listener)
    }

    fun attachDestinationListener(listener: DestinationListener) {
        destinationListeners.add(listener)
    }

    fun detachDestinationListener(listener: DestinationListener) {
        destinationListeners.remove(listener)
    }

    fun postPermissionResult(requestCode: Int, granted: Boolean) {
        resolutionListeners.forEach {
            it.onPermissionResult(requestCode, granted)
        }
    }

    fun postActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == REQUEST_CODE_PICKUP_SEARCH && resultCode == Activity.RESULT_OK && data != null ->
                pickUpListeners.forEach {
                    it.onPickUpReceived(data.getParcelableExtra(AddressSearchActivity.RESULT_ADDRESS))
                }
            requestCode == REQUEST_CODE_DESTINATION_SEARCH && resultCode == Activity.RESULT_OK && data != null ->
                destinationListeners.forEach {
                    it.onDestinationReceived(data.getParcelableExtra(AddressSearchActivity.RESULT_ADDRESS))
                }
            else ->
                resolutionListeners.forEach {
                    it.onActivityResult(requestCode, resultCode)
                }
        }
    }

    private fun shouldShowRationaleLocation(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, ACCESS_FINE_LOCATION)
    }

    interface ResolutionListener {
        fun onPermissionResult(requestCode: Int, granted: Boolean)
        fun onActivityResult(requestCode: Int, resultCode: Int)
    }

    interface PickUpListener {
        fun onPickUpReceived(address: Address)
    }

    interface DestinationListener {
        fun onDestinationReceived(address: Address)
    }
}
