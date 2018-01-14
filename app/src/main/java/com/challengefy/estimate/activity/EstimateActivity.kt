package com.challengefy.estimate.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewGroup
import com.challengefy.R
import com.challengefy.base.activity.BaseActivity
import com.challengefy.destination.activity.DestinationActivity
import com.challengefy.estimate.fragment.AddressesFragment
import com.challengefy.estimate.viewmodel.EstimateViewModel
import com.challengefy.map.fragment.MapFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class EstimateActivity : BaseActivity() {

    companion object {
        private const val REQUEST_CODE_PERMISSION = 1

        fun startIntent(context: Context) = Intent(context, EstimateActivity::class.java)
    }

    @Inject
    lateinit var viewModel: EstimateViewModel

    private val disposables = CompositeDisposable()

    private val ctnLocationDenied by lazy { findViewById<View>(R.id.estimate_ctn_location_permission_denied) }
    private val ctnRoot by lazy { findViewById<ViewGroup>(R.id.estimate_ctn_root) }

    private val mapFragment = MapFragment.newInstance()
    private val destinationFragment = AddressesFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimate)

        initMapFragment()
        initDestinationFragment()
    }

    override fun onResume() {
        super.onResume()
        checkForPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            val result = grantResults.firstOrNull() ?: PERMISSION_DENIED
            when (result) {
                PERMISSION_GRANTED -> initLocation()
                PERMISSION_DENIED -> showLocationPermissionDenied()
            }
        }
    }

    private fun showLocationPermissionDenied() {
        TransitionManager.beginDelayedTransition(ctnRoot)
        ctnLocationDenied.visibility = View.VISIBLE
    }

    private fun initMapFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.estimate_container_map, mapFragment)
                .commit()
    }

    private fun initDestinationFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.estimate_container_content, destinationFragment)
                .commit()
    }

    private fun checkForPermission() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            initLocation()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), REQUEST_CODE_PERMISSION)
        }
    }

    private fun initLocation() {
        mapFragment.activateMyLocation()
        viewModel.location()
                .subscribe(
                        {
                            mapFragment.centerMap(it.latitude, it.longitude)
                        },
                        {
                            Timber.d(it, "Could not Retrieve location")
                        }
                )
                .addDisposable()
    }

    private fun Disposable.addDisposable() {
        disposables.add(this)
    }

    @SuppressLint("RestrictedApi")
    fun goToDestination(cardDestination: CardView) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(cardDestination, "name"))
        startActivityForResult(DestinationActivity.startIntent(this), 0, options.toBundle())
    }
}
