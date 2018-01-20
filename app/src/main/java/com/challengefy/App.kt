package com.challengefy

import android.app.Activity
import android.app.Application
import com.challengefy.base.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber
import javax.inject.Inject

open class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector : DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var loggingTree: Timber.Tree

    override fun onCreate() {
        super.onCreate()

        initializeDagger()
        initializeLogger()
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    private fun initializeLogger() {
        Timber.plant(loggingTree)

        RxJavaPlugins.setErrorHandler {
            if (!BuildConfig.DEBUG) {
                Timber.e(it)
            }
        }
    }

    protected open fun initializeDagger() {
        DaggerApplicationComponent.builder()
                .context(this)
                .build()
                .injectMembers(this)
    }
}
