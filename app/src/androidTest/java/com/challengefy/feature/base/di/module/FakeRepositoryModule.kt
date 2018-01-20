package com.challengefy.feature.base.di.module

import com.challengefy.data.repository.*
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Singleton

@Module
@Singleton
class FakeRepositoryModule {

    @Provides
    fun locationRepository(positionRepositoryImpl: LocationRepositoryImpl): LocationRepository {
        return Mockito.spy(positionRepositoryImpl)
    }

    @Provides
    fun placeRepository(placeRepositoryImpl: PlaceRepositoryImpl): PlaceRepository {
        return Mockito.spy(placeRepositoryImpl)
    }

    @Provides
    fun rideRepository(rideRepositoryImpl: RideRepositoryImpl): RideRepository {
        return Mockito.spy(rideRepositoryImpl)
    }

    @Provides
    fun provideMocks(
            locationRepository: LocationRepository,
            placeRepository: PlaceRepository,
            rideRepository: RideRepository
    ) = Holder(
            locationRepository,
            placeRepository,
            rideRepository
    )

    class Holder(
            val locationRepository: LocationRepository,
            val placeRepository: PlaceRepository,
            val rideRepository: RideRepository
    )
}
