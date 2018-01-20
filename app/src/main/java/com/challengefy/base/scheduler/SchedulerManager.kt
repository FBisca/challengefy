package com.challengefy.base.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SchedulerManager {
    fun mainThread(): Scheduler
    fun ioThread(): Scheduler
    fun timeScheduler(): Scheduler
}

class AppSchedulerManager @Inject constructor(): SchedulerManager {
    override fun mainThread(): Scheduler = AndroidSchedulers.mainThread()
    override fun ioThread(): Scheduler = Schedulers.io()
    override fun timeScheduler(): Scheduler = Schedulers.io()
}
