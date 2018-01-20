package com.challengefy.feature.address.activity

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.runner.AndroidJUnit4
import com.challengefy.R
import com.challengefy.feature.TestApp
import com.challengefy.feature.address.adapter.AddressSearchAdapter
import com.challengefy.feature.test.InjectionTestRule
import com.challengefy.feature.test.KotlinArgumentMatchers.any
import com.challengefy.feature.test.RecyclerViewMatchers
import com.challengefy.test.Fabricator
import io.reactivex.Single
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class AddressSearchActivityTest {
    @Rule
    @JvmField
    val rule = InjectionTestRule(AddressSearchActivity::class.java) {

    }

    val app by lazy { InstrumentationRegistry.getTargetContext().applicationContext as TestApp }

    @Test
    fun testShouldShowItemsCorrectly() {
        val addressList = listOf(
                Fabricator.predictionAddress("Title 1", "Desc 1"),
                Fabricator.predictionAddress("Title 2", "Desc 2")
        )

        `when`(app.dataSourceHolder.placeSource.autoCompletePlaces(any(), any(), any()))
                .thenReturn(Single.just(addressList))

        rule.launchActivity(AddressSearchActivity.startIntent(InstrumentationRegistry.getTargetContext(), null))

        onView(withId(R.id.address_search_inp_address))
                .perform(typeText("Address X"))

        waitForDebounce()

        onView(
                RecyclerViewMatchers.withRecyclerView(R.id.address_search_list)
                .atPosition(0)
                .onView(R.id.item_address_txt_title)
        ).check(matches(withText("Title 1")))

        onView(
                RecyclerViewMatchers.withRecyclerView(R.id.address_search_list)
                        .atPosition(0)
                        .onView(R.id.item_address_txt_description)
        ).check(matches(withText("Desc 1")))
    }

    @Test
    fun testShouldReturnSelectedOnClick() {
        val addressList = listOf(
                Fabricator.predictionAddress("Title 1", "Desc 1"),
                Fabricator.predictionAddress("Title 2", "Desc 2")
        )

        val address = Fabricator.address("Title 1", "Desc 1")

        `when`(app.dataSourceHolder.placeSource.autoCompletePlaces(any(), any(), any()))
                .thenReturn(Single.just(addressList))

        `when`(app.dataSourceHolder.placeSource.detailPrediction(addressList.first()))
                .thenReturn(Single.just(address))

        Intents.init()

        rule.launchActivity(AddressSearchActivity.startIntent(InstrumentationRegistry.getTargetContext(), null))

        onView(withId(R.id.address_search_inp_address))
                .perform(typeText("Address X"))

        waitForDebounce()

        onView(withId(R.id.address_search_list))
                .perform(actionOnItemAtPosition<AddressSearchAdapter.ViewHolder>(0, click()))

        assertTrue(rule.activity.isFinishing)
    }

    private fun waitForDebounce() {
        Thread.sleep(600)
    }
}
