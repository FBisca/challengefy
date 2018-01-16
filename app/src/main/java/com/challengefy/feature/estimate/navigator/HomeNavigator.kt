package com.challengefy.feature.estimate.navigator

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.feature.estimate.activity.HomeActivity
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
    }

    private val listeners = mutableSetOf<ResolutionListener>()

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

    private fun shouldShowRationaleLocation(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, ACCESS_FINE_LOCATION)
    }

    fun goToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.packageName, null))
        activity.startActivityForResult(intent, REQUEST_CODE_APP_SETTINGS)
    }

    fun goToLocationSettings() {
        activity.startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CODE_LOCATION_SETTINGS)
    }

    fun attachResultListener(listener: ResolutionListener) {
        listeners.add(listener)
    }

    fun detachResultListener(listener: ResolutionListener) {
        listeners.remove(listener)
    }

    fun postPermissionResult(requestCode: Int, granted: Boolean) {
        listeners.forEach {
            it.onPermissionResult(requestCode, granted)
        }
    }

    fun postActivityResult(requestCode: Int, resultCode: Int) {
        listeners.forEach {
            it.onActivityResult(requestCode, resultCode)
        }
    }

    interface ResolutionListener {
        fun onPermissionResult(requestCode: Int, granted: Boolean)
        fun onActivityResult(requestCode: Int, resultCode: Int)
    }
}
