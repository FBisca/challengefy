package com.challengefy.base.di.module

import com.challengefy.data.repository.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
@Singleton
abstract class RepositoryModule {

    @Binds
    abstract fun positionRepository(positionRepositoryImpl: PositionRepositoryImpl): PositionRepository

    @Binds
    abstract fun placeRepository(placeRepositoryImpl: PlaceRepositoryImpl): PlaceRepository

    @Binds
    abstract fun rideRepository(rideRepositoryImpl: RideRepositoryImpl): RideRepository
}
