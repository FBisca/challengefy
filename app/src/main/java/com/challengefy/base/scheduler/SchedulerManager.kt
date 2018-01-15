package com.challengefy.base.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SchedulerManager {
    fun mainThread(): Scheduler
    fun ioThread(): Scheduler
}

class AppSchedulerManager @Inject constructor(): SchedulerManager {
    override fun mainThread() = AndroidSchedulers.mainThread()
    override fun ioThread() = Schedulers.io()
}
