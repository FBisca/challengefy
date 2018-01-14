package com.challengefy.base.di.module

import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.destination.activity.AddressSearchActivity
import com.challengefy.destination.module.DestinationModule
import com.challengefy.estimate.activity.HomeActivity
import com.challengefy.estimate.module.EstimateModule
import com.challengefy.home.activity.SplashActivity
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
    @ContributesAndroidInjector(modules = [DestinationModule::class])
    abstract fun injectorDestinationActivity(): AddressSearchActivity
}
