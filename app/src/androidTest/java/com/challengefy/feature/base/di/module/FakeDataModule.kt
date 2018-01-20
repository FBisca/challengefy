package com.challengefy.feature.base.di.module

import com.challengefy.base.di.module.DataSourceModule
import com.challengefy.base.di.module.NetworkModule
import com.challengefy.base.scheduler.AppSchedulerManager
import com.challengefy.base.scheduler.SchedulerManager
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Singleton
@Module(includes = [
    DataSourceModule::class,
    FakeRepositoryModule::class,
    NetworkModule::class
])
abstract class FakeDataModule {
    @Binds
    abstract fun bindsSchedulerManager(schedulerManager: AppSchedulerManager): SchedulerManager
}
