package com.challengefy.feature.estimate.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v7.widget.CardView
import com.challengefy.R
import com.challengefy.base.activity.BaseActivity
import com.challengefy.data.model.Address
import com.challengefy.feature.address.activity.AddressSearchActivity
import com.challengefy.feature.estimate.fragment.EstimateFragment
import com.challengefy.feature.estimate.fragment.PickupFragment
import com.challengefy.feature.estimate.navigator.HomeNavigator
import com.challengefy.feature.estimate.viewmodel.HomeViewModel
import com.challengefy.feature.map.fragment.MapFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class HomeActivity : BaseActivity(), HasSupportFragmentInjector {

    companion object {
        private const val REQUEST_CODE_PERMISSION = 1
        private const val REQUEST_CODE_DESTINATION = 2

        fun startIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModel: HomeViewModel

    @Inject
    lateinit var homeNavigator: HomeNavigator

    private val mapFragment = MapFragment.newInstance()
    private val pickupFragment = PickupFragment()

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
//        checkForPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val result = grantResults.firstOrNull() ?: PERMISSION_DENIED
        homeNavigator.postPermissionResult(requestCode, PERMISSION_GRANTED == result)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        homeNavigator.postActivityResult(requestCode, resultCode)
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


    private fun initMapFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.estimate_container_map, mapFragment)
                .commit()
    }

    private fun initDestinationFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.estimate_container_content, pickupFragment)
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
    }

    @SuppressLint("RestrictedApi")
    fun goToDestination(cardDestination: CardView) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair.create(cardDestination, getString(R.string.transition_address_search))
        )
        startActivityForResult(AddressSearchActivity.startIntent(this, destination), REQUEST_CODE_DESTINATION, options.toBundle())
    }

    override fun supportFragmentInjector() = fragmentInjector
}
