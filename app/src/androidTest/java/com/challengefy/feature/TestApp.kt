package com.challengefy.feature

import com.challengefy.App
import com.challengefy.feature.base.di.component.DaggerTestApplicationComponent
import com.challengefy.feature.base.di.module.FakeNetworkModule
import okhttp3.mockwebserver.MockWebServer

class TestApp : App() {

    val mockWebServer = MockWebServer()

    override fun initializeDagger() {
        DaggerTestApplicationComponent
                .builder()
                .context(this)
                .networkModule(FakeNetworkModule(mockWebServer))
                .build()
                .injectMembers(this)
    }
}
