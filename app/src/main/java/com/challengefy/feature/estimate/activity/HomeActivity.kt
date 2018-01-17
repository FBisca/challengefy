package com.challengefy.feature.estimate.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.databinding.Observable
import android.os.Bundle
import android.support.v4.app.Fragment
import com.challengefy.R
import com.challengefy.base.activity.BaseActivity
import com.challengefy.data.model.Address
import com.challengefy.feature.estimate.fragment.ConfirmPickupFragment
import com.challengefy.feature.estimate.fragment.DestinationFragment
import com.challengefy.feature.estimate.fragment.EstimateFragment
import com.challengefy.feature.estimate.fragment.PickupFragment
import com.challengefy.feature.estimate.navigator.HomeNavigator
import com.challengefy.feature.estimate.viewmodel.HomeViewModel
import com.challengefy.feature.map.fragment.MapFragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class HomeActivity : BaseActivity(), HasSupportFragmentInjector {

    companion object {
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

    private val viewStateListener = ViewStateChangeListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        initMapFragment()
        initPickupFragment()
        viewModel.viewState.addOnPropertyChangedCallback(viewStateListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.viewState.removeOnPropertyChangedCallback(viewStateListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val result = grantResults.firstOrNull() ?: PERMISSION_DENIED
        homeNavigator.postPermissionResult(requestCode, PERMISSION_GRANTED == result)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        homeNavigator.postActivityResult(requestCode, resultCode, data)
    }

    override fun supportFragmentInjector() = fragmentInjector

    private fun initMapFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.estimate_container_map, mapFragment)
                .commit()
    }

    private fun initPickupFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.estimate_container_content, pickupFragment)
                .commit()
    }

    private fun showDestinationFragment(pickUpAddress: Address) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.estimate_container_content, DestinationFragment.newInstance(pickUpAddress))
                .addToBackStack(null)
                .commit()
    }

    private fun showEstimateFragment(pickUpAddress: Address, destinationAddress: Address) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.estimate_container_content, EstimateFragment.newInstance(pickUpAddress, destinationAddress))
                .addToBackStack(null)
                .commit()
    }

    private fun showConfirmPickupFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.estimate_container_content, ConfirmPickupFragment.newInstance())
                .addToBackStack(null)
                .commit()
    }

    inner class ViewStateChangeListener : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable, propertyId: Int) {
            val viewState = viewModel.viewState.get()
            when (viewState) {
                HomeViewModel.ViewState.DESTINATION -> showDestinationFragment(viewModel.pickupAddress.get())
                HomeViewModel.ViewState.ESTIMATE -> showEstimateFragment(viewModel.pickupAddress.get(), viewModel.destinationAddress.get())
                HomeViewModel.ViewState.CONFIRM_PICKUP -> showConfirmPickupFragment()
                else -> Unit // Do Nothing
            }
        }
    }
}
