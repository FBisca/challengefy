package com.challengefy.feature.base.di.module

import com.challengefy.base.di.module.NetworkModule
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer

class FakeNetworkModule(
        private val mockWebServer: MockWebServer
) : NetworkModule() {

    override fun providesBaseUrl(): HttpUrl {
        return mockWebServer.url("/")
    }
}
