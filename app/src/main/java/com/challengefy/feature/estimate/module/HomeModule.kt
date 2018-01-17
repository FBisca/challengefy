package com.challengefy.feature.estimate.module

import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.base.di.scope.FragmentScope
import com.challengefy.feature.estimate.fragment.*
import com.challengefy.feature.map.fragment.MapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@ActivityScope
@Module
abstract class HomeModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectorEstimateFragment(): EstimateFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectorPickupFragment(): PickupFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectorDestinationFragment(): DestinationFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectorConfirmPickupFragment(): ConfirmPickupFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectorLookingForCarFragment(): LookingForCarFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectorMapFragment(): MapFragment

}
