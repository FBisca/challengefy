package com.challengefy.feature.ride.fragment

import android.databinding.ObservableField
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import com.challengefy.R
import com.challengefy.data.model.Address
import com.challengefy.data.model.Estimate
import com.challengefy.feature.ride.bindings.MapPaddingBinding
import com.challengefy.feature.ride.navigator.HomeNavigator
import com.challengefy.feature.ride.viewmodel.ConfirmPickupViewModel
import com.challengefy.feature.ride.viewmodel.HomeViewModel
import com.challengefy.feature.test.FragmentTestRule
import com.challengefy.test.Fabricator
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class ConfirmPickupFragmentTest {

    val homeNavigator: HomeNavigator = Mockito.mock(HomeNavigator::class.java)

    val homeViewModel: HomeViewModel = Mockito.mock(HomeViewModel::class.java)

    val destinationAddress = ObservableField<Address>()

    val pickupAddress = ObservableField<Address>()

    val estimateSelected = ObservableField<Estimate>()

    @JvmField
    @Rule
    val rule = FragmentTestRule(ConfirmPickupFragment.newInstance()) {
        val fragment = it as ConfirmPickupFragment
        fragment.viewModel = ConfirmPickupViewModel(homeNavigator, homeViewModel)
        fragment.mapPaddingBinding = MapPaddingBinding()
    }

    @Before
    fun setUp() {
        `when`(homeViewModel.pickUpAddress).thenReturn(pickupAddress)
        `when`(homeViewModel.destinationAddress).thenReturn(destinationAddress)
        `when`(homeViewModel.estimateSelected).thenReturn(estimateSelected)
    }

    @Test
    fun testPickupAndDestinationDisplayedCorrectly() {
        val pickUp = Fabricator.address("Title 1")
        val destination = Fabricator.address("Title 2")
        pickupAddress.set(pickUp)
        destinationAddress.set(destination)

        rule.launch()

        onView(withId(R.id.pickup_txt_address)).check(matches(withText(pickUp.title)))
        onView(withId(R.id.destination_txt_address)).check(matches(withText(destination.title)))
    }

    @Test
    fun testEstimateDisplayedCorrectly() {
        val estimate = Fabricator.estimate()
        estimateSelected.set(estimate)

        rule.launch()

        onView(withId(R.id.estimate_txt_vehicle)).check(matches(withText(estimate.vehicle.shortName)))
        onView(withId(R.id.estimate_txt_value)).check(matches(withText(estimate.price)))
    }

    @Test
    fun testButtonShouldBeVisible() {
        rule.launch()

        onView(withId(R.id.estimate_btn_request)).check(matches(isDisplayed()))
    }
}
