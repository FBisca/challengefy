package com.challengefy.feature.ride.fragment

import android.databinding.ObservableField
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import com.challengefy.R
import com.challengefy.data.model.Address
import com.challengefy.feature.ride.bindings.MapPaddingBinding
import com.challengefy.feature.ride.navigator.HomeNavigator
import com.challengefy.feature.ride.viewmodel.DestinationViewModel
import com.challengefy.feature.ride.viewmodel.HomeViewModel
import com.challengefy.feature.test.FragmentTestRule
import com.challengefy.test.Fabricator
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class DestinationFragmentTest {

    val homeNavigator: HomeNavigator = Mockito.mock(HomeNavigator::class.java)

    val homeViewModel: HomeViewModel = Mockito.mock(HomeViewModel::class.java)

    val destinationAddress = ObservableField<Address>()

    val pickupAddress = ObservableField<Address>()

    @JvmField
    @Rule
    val rule = FragmentTestRule(DestinationFragment.newInstance()) {
        val fragment = it as DestinationFragment
        fragment.viewModel = DestinationViewModel(homeNavigator, homeViewModel)
        fragment.mapPaddingBinding = MapPaddingBinding()
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        `when`(homeViewModel.pickUpAddress).thenReturn(pickupAddress)
        `when`(homeViewModel.destinationAddress).thenReturn(destinationAddress)

    }

    @Test
    fun testPickupAddressDisplayedCorrectly() {
        val address = Fabricator.address()
        pickupAddress.set(address)
        destinationAddress.set(null)

        rule.launch()

        onView(withId(R.id.pickup_txt_address)).check(matches(withText(address.title)))
        onView(withId(R.id.destination_txt_address)).check(matches(withText("")))
    }

    @Test
    fun testDestinationAddressDisplayedCorrectly() {
        val address = Fabricator.address()
        destinationAddress.set(address)

        rule.launch()

        onView(withId(R.id.destination_txt_address)).check(matches(withText(address.title)))
    }

    @Test
    fun testButtonShouldBeDisabledWhenDestinationIsNull() {
        destinationAddress.set(null)

        rule.launch()

        onView(withId(R.id.estimate_btn_confirm)).check(matches(not(isEnabled())))
    }

    @Test
    fun testButtonShouldBeEnabledWhenDestinationIsSet() {
        destinationAddress.set(Fabricator.address())

        rule.launch()

        onView(withId(R.id.estimate_btn_confirm)).check(matches(isEnabled()))
    }
}
