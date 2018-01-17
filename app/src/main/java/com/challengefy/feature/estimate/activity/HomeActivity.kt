package com.challengefy.feature.estimate.activity

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
        const val FADE_DURATION = 300L
        const val TRANSITION_DURATION = 600L

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

        viewModel.init()
        viewModel.viewState.addOnPropertyChangedCallback(viewStateListener)
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

    private fun showDestinationFragment() {
        val previousFragment = supportFragmentManager.findFragmentById(R.id.estimate_container_content)
        val fragment = DestinationFragment.newInstance()

        val exitFade = Fade()
        exitFade.duration = 300

        previousFragment.exitTransition = exitFade

        val enterTransitionSet = AutoTransition()
        enterTransitionSet.duration = 300
        enterTransitionSet.startDelay = 300

        val returnTransition = AutoTransition()
        returnTransition.duration = 300
        returnTransition.startDelay = 900

        val enterFade = Fade()
        enterFade.startDelay = 300
        enterFade.duration = 300

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
        val fragment = EstimateFragment.newInstance()

        val exitFade = Fade()
        exitFade.duration = 300

        previousFragment.exitTransition = exitFade

        val enterTransitionSet = AutoTransition()
        enterTransitionSet.duration = 600
        enterTransitionSet.startDelay = 300

        val returnTransition = AutoTransition()
        returnTransition.duration = 600
        returnTransition.startDelay = 900

        val enterFade = Fade()
        enterFade.startDelay = 300
        enterFade.duration = 600

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
        val fragment = ConfirmPickupFragment.newInstance()

        val exitFade = Fade()
        exitFade.duration = FADE_DURATION

        val enterTransitionSet = AutoTransition()
        enterTransitionSet.duration = TRANSITION_DURATION
        enterTransitionSet.startDelay = FADE_DURATION

        val enterFade = Fade()
        enterFade.duration = FADE_DURATION
        enterFade.startDelay = TRANSITION_DURATION + FADE_DURATION

        previousFragment.exitTransition = exitFade
        fragment.enterTransition = enterFade
        fragment.sharedElementEnterTransition = enterTransitionSet

        supportFragmentManager.beginTransaction()
                .addSharedElement(findViewById(R.id.estimate_card_pickup), getString(R.string.transition_pickup))
                .addSharedElement(findViewById(R.id.estimate_card_container), getString(R.string.transition_bottom_card))
                .replace(R.id.estimate_container_content, fragment)
                .addToBackStack(null)
                .commit()
    }

    inner class ViewStateChangeListener : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable, propertyId: Int) {
            val viewState = viewModel.viewState.get()
            when (viewState) {
                HomeViewModel.ViewState.DESTINATION -> showDestinationFragment()
                HomeViewModel.ViewState.ESTIMATE -> showEstimateFragment()
                HomeViewModel.ViewState.CONFIRM_PICKUP -> showConfirmPickupFragment()
                else -> Unit // Do Nothing
            }
        }
    }
}
