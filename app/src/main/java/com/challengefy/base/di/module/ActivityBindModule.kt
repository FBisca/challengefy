package com.challengefy.base.di.module

import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.feature.address.activity.AddressSearchActivity
import com.challengefy.feature.launch.activity.SplashActivity
import com.challengefy.feature.ride.activity.HomeActivity
import com.challengefy.feature.ride.module.HomeModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectorSplashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun injectorHomeActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun injectorAddressSerchActivity(): AddressSearchActivity
}
