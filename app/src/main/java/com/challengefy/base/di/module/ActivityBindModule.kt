package com.challengefy.base.di.module

import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.feature.address.activity.AddressSearchActivity
import com.challengefy.feature.address.module.AddressSearchModule
import com.challengefy.feature.estimate.activity.HomeActivity
import com.challengefy.feature.estimate.module.HomeModule
import com.challengefy.feature.launch.activity.SplashActivity
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
    @ContributesAndroidInjector(modules = [AddressSearchModule::class])
    abstract fun injectorAddressSerchActivity(): AddressSearchActivity
}
