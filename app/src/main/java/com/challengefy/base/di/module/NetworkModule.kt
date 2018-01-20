package com.challengefy.base.di.module

import com.challengefy.BuildConfig
import com.challengefy.base.util.OpenForTests
import com.challengefy.data.network.interceptors.AuthInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Singleton
@Module(includes = [ApiModule::class])
@OpenForTests
class NetworkModule {

    @Provides
    fun provideRetrofit(client: OkHttpClient, httpUrl: HttpUrl): Retrofit {
        return Retrofit.Builder()
                .client(client)
                .baseUrl(httpUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    fun providesBaseUrl(): HttpUrl {
        return HttpUrl.parse(BuildConfig.BASE_URL) ?: throw IllegalArgumentException("BaseUrl is null")
    }

    @Provides
    fun provideOkHttpClient(
            authInterceptor: AuthInterceptor,
            loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
    }
}
