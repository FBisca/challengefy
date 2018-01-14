package com.challengefy.base.di.module

import com.challengefy.data.network.api.EstimateApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@Singleton
class ApiModule {

    @Provides
    fun providesEstimateApi(retrofit: Retrofit): EstimateApi {
        return retrofit.create(EstimateApi::class.java)
    }
}
