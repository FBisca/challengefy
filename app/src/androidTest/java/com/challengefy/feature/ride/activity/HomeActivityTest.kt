package com.challengefy.feature.ride.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.challengefy.R
import com.challengefy.data.model.Address
import com.challengefy.data.model.Position
import com.challengefy.feature.TestApp
import com.challengefy.feature.base.scheduler.TestSchedulerManager
import com.challengefy.test.Fabricator
import com.challengefy.test.KotlinArgumentMatchers.any
import io.reactivex.Flowable
import io.reactivex.Single
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @Rule
    @JvmField
    val grantPermissionRule = GrantPermissionRule.grant(ACCESS_FINE_LOCATION)

    @Rule
    @JvmField
    val rule = IntentsTestRule(HomeActivity::class.java, false, false)

    val app by lazy { InstrumentationRegistry.getTargetContext().applicationContext as TestApp }

    @Test
    fun testShouldShowLocationDisabled() {
        `when`(app.dataSourceHolder.locationSource.isLocationEnabled(any()))
                .thenReturn(Single.just(false))

        `when`(app.dataSourceHolder.locationSource.isPermissionGranted())
                .thenReturn(Single.just(true))

        `when`(app.dataSourceHolder.placeSource.getCurrentPlace())
                .thenReturn(Single.just(Fabricator.address()))

        `when`(app.dataSourceHolder.locationSource.positionUpdates(any()))
                .thenReturn(Flowable.just(Position(0.0, 0.0)))

        rule.launchActivity(Intent())

        val res = InstrumentationRegistry.getTargetContext().resources

        TestSchedulerManager.testScheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)

        onView(withId(R.id.pickup_txt_location_title))
                .check(matches(isDisplayed()))
                .check(matches(withText(res.getString(R.string.location_disabled))))

        onView(withId(R.id.pickup_txt_location_description))
                .check(matches(isDisplayed()))
                .check(matches(withText(res.getString(R.string.location_disabled_description))))

        onView(withId(R.id.pickup_txt_location_activate))
                .check(matches(isDisplayed()))
    }

    @Test
    fun testPickUpDisplayedCorrectly() {
        val address = Fabricator.address()
        `when`(app.dataSourceHolder.locationSource.isLocationEnabled(any()))
                .thenReturn(Single.just(true))

        `when`(app.dataSourceHolder.locationSource.isPermissionGranted())
                .thenReturn(Single.just(true))

        `when`(app.dataSourceHolder.placeSource.getCurrentPlace())
                .thenReturn(Single.just(address))

        `when`(app.dataSourceHolder.locationSource.positionUpdates(any()))
                .thenReturn(Flowable.just(Position(0.0, 0.0)))

        rule.launchActivity(Intent())

        TestSchedulerManager.testScheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)

        onView(withId(R.id.pickup_txt_address))
                .check(matches(withText(address.title)))
    }

    @Test
    fun testPickUpErrorShouldDisplayInputBox() {
        `when`(app.dataSourceHolder.locationSource.isLocationEnabled(any()))
                .thenReturn(Single.just(true))

        `when`(app.dataSourceHolder.locationSource.isPermissionGranted())
                .thenReturn(Single.just(true))

        `when`(app.dataSourceHolder.placeSource.getCurrentPlace())
                .thenReturn(Single.error<Address>(IllegalArgumentException()))

        rule.launchActivity(Intent())

        TestSchedulerManager.testScheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)

        onView(withId(R.id.pickup_txt_address))
                .check(matches(isDisplayed()))

        onView(withId(R.id.pickup_card_location_permission))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.estimate_btn_confirm))
                .check(matches(isDisplayed()))
    }

}
