package com.challengefy.feature.ride.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.databinding.Observable
import android.os.Bundle
import android.support.transition.AutoTransition
import android.support.transition.Fade
import android.support.v4.app.Fragment
import com.challengefy.R
import com.challengefy.base.activity.BaseActivity
import com.challengefy.data.model.Address
import com.challengefy.data.model.Estimate
import com.challengefy.feature.ride.fragment.*
import com.challengefy.feature.ride.navigator.HomeNavigator
import com.challengefy.feature.ride.viewmodel.HomeViewModel
import com.challengefy.feature.map.fragment.MapFragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class HomeActivity : BaseActivity(), HasSupportFragmentInjector {

    companion object {
        const val FADE_DURATION = 300L
        const val TRANSITION_DURATION = 600L

        private const val BUNDLE_PICKUP = "BUNDLE_PICKUP"
        private const val BUNDLE_DESTINATION = "BUNDLE_DESTINATION"
        private const val BUNDLE_SELECTED_ESTIMATE = "BUNDLE_SELECTED_ESTIMATE"

        fun startIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModel: HomeViewModel

    @Inject
    lateinit var homeNavigator: HomeNavigator

    private val viewStateListener = ViewStateChangeListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        if (savedInstanceState == null) {
            initMapFragment()
            initPickupFragment()
        }

        val pickup = savedInstanceState?.getParcelable<Address>(BUNDLE_PICKUP)
        val destination = savedInstanceState?.getParcelable<Address>(BUNDLE_DESTINATION)
        val estimateSelected = savedInstanceState?.getParcelable<Estimate>(BUNDLE_SELECTED_ESTIMATE)

        viewModel.init(pickup, destination, estimateSelected)
        viewModel.viewState.addOnPropertyChangedCallback(viewStateListener)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        doBack()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.viewState.removeOnPropertyChangedCallback(viewStateListener)
        viewModel.dispose()
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(BUNDLE_PICKUP, viewModel.pickUpAddress.get())
        outState?.putParcelable(BUNDLE_DESTINATION, viewModel.destinationAddress.get())
        outState?.putParcelable(BUNDLE_SELECTED_ESTIMATE, viewModel.estimateSelected.get())
    }

    override fun supportFragmentInjector() = fragmentInjector

    private fun initMapFragment() {
        val mapFragment = findFragment() ?: MapFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.estimate_container_map, mapFragment)
                .commit()
    }

    private fun initPickupFragment() {
        val pickupFragment = findFragment() ?: PickupFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.estimate_container_content, pickupFragment)
                .commit()
    }

    private fun showDestinationFragment() {
        val previousFragment = supportFragmentManager.findFragmentById(R.id.estimate_container_content)
        val fragment = findFragment() ?: DestinationFragment.newInstance()

        val exitFade = Fade()
        exitFade.duration = FADE_DURATION

        previousFragment.exitTransition = exitFade

        val enterTransitionSet = AutoTransition()
        enterTransitionSet.duration = FADE_DURATION
        enterTransitionSet.startDelay = FADE_DURATION

        val returnTransition = AutoTransition()
        returnTransition.duration = FADE_DURATION
        returnTransition.startDelay = FADE_DURATION + TRANSITION_DURATION

        val enterFade = Fade()
        enterFade.startDelay = FADE_DURATION
        enterFade.duration = FADE_DURATION

        fragment.enterTransition = enterFade
        fragment.returnTransition = exitFade
        fragment.sharedElementEnterTransition = enterTransitionSet
        fragment.sharedElementReturnTransition = returnTransition

        supportFragmentManager.beginTransaction()
                .addSharedElement(findViewById(R.id.pickup_card), getString(R.string.transition_pickup))
                .addSharedElement(findViewById(R.id.estimate_btn_confirm), getString(R.string.transition_btn_confirm))
                .replace(R.id.estimate_container_content, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun showEstimateFragment() {
        val previousFragment = supportFragmentManager.findFragmentById(R.id.estimate_container_content)
        val fragment = findFragment() ?: EstimateFragment.newInstance()

        val exitFade = Fade()
        exitFade.duration = FADE_DURATION

        val enterTransitionSet = AutoTransition()
        enterTransitionSet.duration = TRANSITION_DURATION
        enterTransitionSet.startDelay = FADE_DURATION

        val returnTransition = AutoTransition()
        returnTransition.duration = TRANSITION_DURATION
        returnTransition.startDelay = FADE_DURATION + TRANSITION_DURATION

        val enterFade = Fade()
        enterFade.startDelay = FADE_DURATION
        enterFade.duration = TRANSITION_DURATION

        previousFragment.exitTransition = exitFade
        fragment.enterTransition = enterFade
        fragment.returnTransition = exitFade
        fragment.sharedElementEnterTransition = enterTransitionSet
        fragment.sharedElementReturnTransition = returnTransition

        supportFragmentManager.beginTransaction()
                .addSharedElement(findViewById(R.id.estimate_card_pickup), getString(R.string.transition_pickup))
                .addSharedElement(findViewById(R.id.estimate_card_destination), getString(R.string.transition_destination))
                .replace(R.id.estimate_container_content, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun showConfirmPickupFragment() {
        val previousFragment = supportFragmentManager.findFragmentById(R.id.estimate_container_content)
        val fragment = findFragment() ?: ConfirmPickupFragment.newInstance()

        val exitFade = Fade()
        exitFade.duration = FADE_DURATION

        val enterTransitionSet = AutoTransition()
        enterTransitionSet.duration = TRANSITION_DURATION
        enterTransitionSet.startDelay = FADE_DURATION

        val enterFade = Fade()
        enterFade.duration = FADE_DURATION
        enterFade.startDelay = TRANSITION_DURATION + FADE_DURATION

        previousFragment.exitTransition = exitFade
        previousFragment.reenterTransition = enterFade
        fragment.enterTransition = enterFade
        fragment.returnTransition = exitFade
        fragment.sharedElementEnterTransition = enterTransitionSet

        supportFragmentManager.beginTransaction()
                .addSharedElement(findViewById(R.id.estimate_card_pickup), getString(R.string.transition_pickup))
                .addSharedElement(findViewById(R.id.estimate_card_destination), getString(R.string.transition_destination))
                .replace(R.id.estimate_container_content, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun showFindingCar() {
        val previousFragment = supportFragmentManager.findFragmentById(R.id.estimate_container_content)
        val fragment = findFragment() ?: LookingForCarFragment.newInstance()

        val exitFade = Fade()
        exitFade.duration = FADE_DURATION

        val enterFade = Fade()
        enterFade.duration = FADE_DURATION
        enterFade.startDelay = FADE_DURATION

        previousFragment.exitTransition = exitFade
        previousFragment.reenterTransition = enterFade
        fragment.enterTransition = enterFade
        fragment.returnTransition = exitFade

        supportFragmentManager.beginTransaction()
                .addSharedElement(findViewById(R.id.estimate_card_pickup), getString(R.string.transition_pickup))
                .addSharedElement(findViewById(R.id.estimate_card_destination), getString(R.string.transition_destination))
                .replace(R.id.estimate_container_content, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun doBack() {
        viewModel.viewState.removeOnPropertyChangedCallback(viewStateListener)
        viewModel.onBack()
        viewModel.viewState.addOnPropertyChangedCallback(viewStateListener)
    }

    inner class ViewStateChangeListener : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable, propertyId: Int) {
            val viewState = viewModel.viewState.get()
            when (viewState) {
                HomeViewModel.ViewState.DESTINATION -> showDestinationFragment()
                HomeViewModel.ViewState.ESTIMATE -> showEstimateFragment()
                HomeViewModel.ViewState.CONFIRM_PICKUP -> showConfirmPickupFragment()
                HomeViewModel.ViewState.LOOKING_FOR_CAR -> showFindingCar()
                else -> Unit // Do Nothing
            }
        }

    }
}
