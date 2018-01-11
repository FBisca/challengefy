package com.challengefy.base.di.module

import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.estimate.activity.EstimateActivity
import com.challengefy.home.activity.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectorSplashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectorEstimateActivity(): EstimateActivity
}
