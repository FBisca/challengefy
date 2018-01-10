package com.challengefy

import android.app.Activity
import android.app.Application
import com.challengefy.base.di.component.DaggerApplicationComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector : DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        initializeDagger()
    }

    private fun initializeDagger() {
        DaggerApplicationComponent.builder()
                .context(this)
                .build()
                .injectMembers(this)
    }

    override fun activityInjector() = activityInjector
}