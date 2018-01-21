package com.challengefy.feature.base.scheduler

import com.challengefy.base.scheduler.SchedulerManager
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import javax.inject.Inject

class TestSchedulerManager @Inject constructor() : SchedulerManager {

    companion object {
        val testScheduler = TestScheduler()
    }

    override fun mainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun ioThread(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun timeScheduler(): Scheduler {
        return testScheduler
    }
}
