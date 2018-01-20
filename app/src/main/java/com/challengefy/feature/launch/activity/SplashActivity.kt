package com.challengefy.feature.launch.activity

import android.os.Bundle
import com.challengefy.base.activity.BaseActivity
import com.challengefy.feature.ride.activity.HomeActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(HomeActivity.startIntent(this))
        finish()
    }
}
