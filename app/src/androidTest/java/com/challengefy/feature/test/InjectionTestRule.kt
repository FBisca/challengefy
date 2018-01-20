package com.challengefy.feature.test

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import com.challengefy.feature.TestApp

class InjectionTestRule<T: Activity>(
        clazz: Class<T>,
        private val injectionListener: (Activity) -> Unit
) : ActivityTestRule<T>(clazz, false, false) {

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        val app = InstrumentationRegistry.getTargetContext().applicationContext as TestApp
        app.injectionListener = injectionListener

    }

    override fun afterActivityFinished() {
        super.afterActivityFinished()
        val app = InstrumentationRegistry.getTargetContext().applicationContext as TestApp
        app.injectionListener = TestApp.DEFAULT_INJECTION_LISTENER
    }
}
