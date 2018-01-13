package com.challengefy.base.di.module

import com.challengefy.data.source.location.FusedPositionSource
import com.challengefy.data.source.location.PositionSource
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
@Singleton
abstract class DataSourceModule {

    @Binds
    abstract fun providesPositionSource(source: FusedPositionSource): PositionSource
}
