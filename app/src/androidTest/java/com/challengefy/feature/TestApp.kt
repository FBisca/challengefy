package com.challengefy.feature

import android.app.Activity
import com.challengefy.App
import com.challengefy.feature.base.di.component.DaggerTestApplicationComponent
import com.challengefy.feature.base.di.module.FakeDataSourceModule
import com.challengefy.feature.base.di.module.FakeNetworkModule
import com.challengefy.feature.base.di.module.FakeRepositoryModule
import dagger.android.AndroidInjector
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Inject

class TestApp : App() {

    companion object {
        val DEFAULT_INJECTION_LISTENER: (Activity) -> Unit = {}
    }

    @Inject
    lateinit var dataSourceHolder: FakeDataSourceModule.Holder

    @Inject
    lateinit var repositoryHolder: FakeRepositoryModule.Holder

    override fun initializeDagger() {
        DaggerTestApplicationComponent
                .builder()
                .context(this)
                .build()
                .injectMembers(this)
    }
}
