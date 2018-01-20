package com.challengefy.feature.ride.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.challengefy.App
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @Rule
    @JvmField
    val grantPermissionRule = GrantPermissionRule.grant(ACCESS_FINE_LOCATION)

    @Rule
    @JvmField
    val rule = ActivityTestRule(HomeActivity::class.java, false, false)

    @Test
    fun testPermission() {
        rule.launchActivity(Intent())
    }
}
