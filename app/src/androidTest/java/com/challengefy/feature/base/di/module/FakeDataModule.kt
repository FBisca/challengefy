package com.challengefy.feature.base.di.module

import com.challengefy.base.di.module.NetworkModule
import com.challengefy.base.scheduler.SchedulerManager
import com.challengefy.feature.base.scheduler.TestSchedulerManager
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Singleton
@Module(includes = [
    FakeDataSourceModule::class,
    FakeRepositoryModule::class,
    NetworkModule::class
])
abstract class FakeDataModule {
    @Binds
    abstract fun bindsSchedulerManager(schedulerManager: TestSchedulerManager): SchedulerManager
}
