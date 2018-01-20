package com.challengefy.test

import com.challengefy.base.scheduler.SchedulerManager
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

class TestSchedulerManager : SchedulerManager {

    val timeScheduler = TestScheduler()

    override fun mainThread(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun ioThread(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun timeScheduler(): Scheduler {
        return timeScheduler
    }
}
