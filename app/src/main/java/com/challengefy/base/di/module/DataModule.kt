package com.challengefy.base.di.module

import com.challengefy.base.scheduler.AppSchedulerManager
import com.challengefy.base.scheduler.SchedulerManager
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [
    DataSourceModule::class,
    RepositoryModule::class,
    NetworkModule::class
])
@Singleton
abstract class DataModule {

    @Binds
    abstract fun bindsSchedulerManager(schedulerManager: AppSchedulerManager): SchedulerManager

}
