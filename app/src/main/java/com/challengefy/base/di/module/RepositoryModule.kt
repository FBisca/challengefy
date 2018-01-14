package com.challengefy.base.di.module

import com.challengefy.data.repository.PlaceRepository
import com.challengefy.data.repository.PlaceRepositoryImpl
import com.challengefy.data.repository.PositionRepository
import com.challengefy.data.repository.PositionRepositoryImpl
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
}
