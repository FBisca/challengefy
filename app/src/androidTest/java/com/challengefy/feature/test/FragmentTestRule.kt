package com.challengefy.feature.test

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import android.support.v4.app.Fragment
import com.challengefy.base.activity.FragmentTestActivity

class FragmentTestRule<T : Fragment>(
        private val fragment: T,
        var injectionListener: ((Fragment) -> Unit)? = null
) : ActivityTestRule<FragmentTestActivity>(
        FragmentTestActivity::class.java,
        false,
        false
) {

    fun launch() {
        launchActivity(Intent())
    }

    override fun afterActivityLaunched() {
        activity?.setFragment(fragment)
        injectionListener?.let {
            activity?.setInjectionListener(it)
        }
    }
}
