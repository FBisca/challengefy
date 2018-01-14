package com.challengefy.feature.estimate.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
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
import com.challengefy.data.model.Address
import com.challengefy.feature.address.activity.AddressSearchActivity
import com.challengefy.feature.estimate.fragment.DestinationFragment
import com.challengefy.feature.estimate.fragment.EstimateFragment
import com.challengefy.feature.estimate.viewmodel.HomeViewModel
import com.challengefy.feature.map.fragment.MapFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class HomeActivity : BaseActivity() {

    companion object {
        private const val REQUEST_CODE_PERMISSION = 1
        private const val REQUEST_CODE_DESTINATION = 2

        fun startIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    @Inject
    lateinit var viewModel: HomeViewModel

    private val disposables = CompositeDisposable()

    private val ctnLocationDenied by lazy { findViewById<View>(R.id.estimate_ctn_location_permission_denied) }
    private val ctnRoot by lazy { findViewById<ViewGroup>(R.id.estimate_ctn_root) }

    private val mapFragment = MapFragment.newInstance()
    private val destinationFragment = DestinationFragment.newInstance()

    private var pickup: Address? = null
    private var destination: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_DESTINATION) {
            val address = data?.getParcelableExtra<Address>(AddressSearchActivity.RESULT_ADDRESS)
            this.destination = address
            address?.let { destinationReceived(address) }
        }
    }

    private fun destinationReceived(destination: Address) {
        pickup?.let {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.estimate_container_content, EstimateFragment.newInstance(it, destination))
                    .commit()
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
        mapFragment.enableCurrentLocation()
        viewModel.currentLocation()
                .subscribe(
                        {
                            pickup = it
                            destinationFragment.showPickup(it)
                            mapFragment.centerMap(it.position.latitude, it.position.longitude)
                        },
                        {

                        }
                )
    }

    private fun Disposable.addDisposable() {
        disposables.add(this)
    }

    @SuppressLint("RestrictedApi")
    fun goToDestination(cardDestination: CardView) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair.create(cardDestination, getString(R.string.transition_address_search))
        )
        startActivityForResult(AddressSearchActivity.startIntent(this, destination), REQUEST_CODE_DESTINATION, options.toBundle())
    }
}
