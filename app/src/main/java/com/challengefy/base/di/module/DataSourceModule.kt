package com.challengefy.base.di.module

import com.challengefy.data.source.location.FusedLocationSource
import com.challengefy.data.source.location.LocationSource
import com.challengefy.data.source.place.GooglePlaceSource
import com.challengefy.data.source.place.PlaceSource
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
@Singleton
abstract class DataSourceModule {

    @Binds
    abstract fun locationSource(source: FusedLocationSource): LocationSource

    @Binds
    abstract fun placesSource(source: GooglePlaceSource): PlaceSource
}
