package com.challengefy.feature.base.di.module

import com.challengefy.data.source.location.LocationSource
import com.challengefy.data.source.place.PlaceSource
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Singleton

@Module
@Singleton
class FakeDataSourceModule {

    @Provides
    @Singleton
    fun locationSource(): LocationSource {
        return Mockito.mock(LocationSource::class.java)
    }

    @Provides
    @Singleton
    fun placesSource(): PlaceSource {
        return Mockito.mock(PlaceSource::class.java)
    }

    @Provides
    @Singleton
    fun providesMocks(locationSource: LocationSource, placeSource: PlaceSource) = Holder(
            locationSource,
            placeSource
    )

    class Holder(
        val locationSource: LocationSource,
        val placeSource: PlaceSource
    )
}
