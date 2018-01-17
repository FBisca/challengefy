package com.challengefy.feature.estimate.module

import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.base.di.scope.FragmentScope
import com.challengefy.feature.estimate.fragment.DestinationFragment
import com.challengefy.feature.estimate.fragment.EstimateFragment
import com.challengefy.feature.estimate.fragment.LookingForCarFragment
import com.challengefy.feature.estimate.fragment.PickupFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@ActivityScope
@Module
abstract class HomeModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [EstimateModule::class])
    abstract fun injectorEstimateFragment(): EstimateFragment

    @FragmentScope
    @ContributesAndroidInjector()
    abstract fun injectorPickupFragment(): PickupFragment

    @FragmentScope
    @ContributesAndroidInjector()
    abstract fun injectorDestinationFragment(): DestinationFragment

    @FragmentScope
    @ContributesAndroidInjector()
    abstract fun injectorLookingForCarFragment(): LookingForCarFragment

}
