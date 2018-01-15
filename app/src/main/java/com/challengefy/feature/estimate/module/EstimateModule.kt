package com.challengefy.feature.estimate.module

import com.challengefy.base.di.scope.FragmentScope
import com.challengefy.feature.estimate.fragment.EstimateFragment
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
@FragmentScope
class EstimateModule {

    @Named("pickup")
    @Provides
    fun providePickup(fragment: EstimateFragment) = fragment.getPickupArgument()

    @Named("destination")
    @Provides
    fun provideDestination(fragment: EstimateFragment) = fragment.getDestinationArgument()
}
