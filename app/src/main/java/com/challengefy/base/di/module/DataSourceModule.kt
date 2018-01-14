package com.challengefy.base.di.module

import com.challengefy.data.source.location.FusedPositionSource
import com.challengefy.data.source.location.PositionSource
import com.challengefy.data.source.place.GooglePlaceSource
import com.challengefy.data.source.place.PlaceSource
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
@Singleton
abstract class DataSourceModule {

    @Binds
    abstract fun positionSource(source: FusedPositionSource): PositionSource

    @Binds
    abstract fun placesSource(source: GooglePlaceSource): PlaceSource
}
