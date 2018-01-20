package com.challengefy.feature.base.di.module

import com.challengefy.base.di.module.DataSourceModule
import com.challengefy.data.source.location.FusedLocationSource
import com.challengefy.data.source.location.LocationSource
import com.challengefy.data.source.place.GooglePlaceSource
import com.challengefy.data.source.place.PlaceSource
import dagger.Module
import org.mockito.Mockito
import javax.inject.Singleton

@Module
@Singleton
class FakeDataSourceModule : DataSourceModule() {
    override fun locationSource(source: FusedLocationSource): LocationSource {
        return Mockito.mock(LocationSource::class.java)
    }

    override fun placesSource(source: GooglePlaceSource): PlaceSource {
        return Mockito.mock(PlaceSource::class.java)
    }
}
