package com.challengefy.base.di.module

import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.feature.address.activity.AddressSearchActivity
import com.challengefy.feature.address.module.AddressSearchModule
import com.challengefy.feature.estimate.activity.HomeActivity
import com.challengefy.feature.estimate.module.EstimateModule
import com.challengefy.feature.home.activity.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectorSplashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [EstimateModule::class])
    abstract fun injectorEstimateActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [AddressSearchModule::class])
    abstract fun injectorDestinationActivity(): AddressSearchActivity
}
