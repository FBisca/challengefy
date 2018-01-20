package com.challengefy.feature.base.di.module

import com.challengefy.base.di.module.NetworkModule
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer
import android.os.StrictMode

class FakeNetworkModule(
        private val mockWebServer: MockWebServer
) : NetworkModule() {

    override fun providesBaseUrl(): HttpUrl {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        return mockWebServer.url("/")
    }
}
