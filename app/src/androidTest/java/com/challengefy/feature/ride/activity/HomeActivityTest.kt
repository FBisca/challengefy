package com.challengefy.feature.ride.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.challengefy.R
import com.challengefy.feature.TestApp
import com.challengefy.feature.test.InjectionTestRule
import com.challengefy.test.KotlinArgumentMatchers.any
import com.challengefy.test.Fabricator
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @Rule
    @JvmField
    val grantPermissionRule = GrantPermissionRule.grant(ACCESS_FINE_LOCATION)

    @Rule
    @JvmField
    val rule = InjectionTestRule(HomeActivity::class.java) {
        val activity = it as HomeActivity
        activity.homeNavigator = Mockito.spy(activity.homeNavigator)
    }

    val app by lazy { InstrumentationRegistry.getTargetContext().applicationContext as TestApp }

    @Test
    fun testShouldShowLocationDisabled() {
        `when`(app.dataSourceHolder.locationSource.isLocationEnabled(any()))
                .thenReturn(Single.just(false))

        `when`(app.dataSourceHolder.locationSource.isPermissionGranted())
                .thenReturn(Single.just(true))

        `when`(app.dataSourceHolder.placeSource.getCurrentPlace())
                .thenReturn(Single.just(Fabricator.address()))

        rule.launchActivity(Intent())

        val res = InstrumentationRegistry.getTargetContext().resources

        onView(withId(R.id.pickup_txt_location_title))
                .check(matches(isDisplayed()))
                .check(matches(withText(res.getString(R.string.location_disabled))))

        onView(withId(R.id.pickup_txt_location_description))
                .check(matches(isDisplayed()))
                .check(matches(withText(res.getString(R.string.location_disabled_description))))

        onView(withId(R.id.pickup_txt_location_activate))
                .check(matches(isDisplayed()))
    }

}
