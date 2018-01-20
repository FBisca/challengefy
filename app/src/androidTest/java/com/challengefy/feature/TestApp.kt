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

    val mockWebServer = MockWebServer()

    @Inject
    lateinit var dataSourceHolder: FakeDataSourceModule.Holder

    @Inject
    lateinit var repositoryHolder: FakeRepositoryModule.Holder

    var injectionListener: (Activity) -> Unit = DEFAULT_INJECTION_LISTENER

    override fun initializeDagger() {
        DaggerTestApplicationComponent
                .builder()
                .context(this)
                .networkModule(FakeNetworkModule(mockWebServer))
                .build()
                .injectMembers(this)
    }

    override fun activityInjector() = AndroidInjector<Activity> {
        activityInjector.maybeInject(it)
        injectionListener(it)
    }
}
