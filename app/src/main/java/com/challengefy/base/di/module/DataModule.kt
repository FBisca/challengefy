package com.challengefy.base.di.module

import dagger.Module
import javax.inject.Singleton

@Module(includes = [
    DataSourceModule::class,
    RepositoryModule::class
])
@Singleton
class DataModule
