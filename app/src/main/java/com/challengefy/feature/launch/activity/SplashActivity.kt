package com.challengefy.feature.launch.activity

import android.os.Bundle
import com.challengefy.Navigator
import com.challengefy.base.activity.BaseActivity
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigator.goToEstimateScreen()
        finish()
    }
}
