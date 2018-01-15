package com.challengefy.feature.estimate.module

import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.feature.estimate.fragment.EstimateFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@ActivityScope
@Module
abstract class HomeModule {

    @ContributesAndroidInjector(modules = [EstimateModule::class])
    abstract fun injectorEstimateFragment(): EstimateFragment

}
