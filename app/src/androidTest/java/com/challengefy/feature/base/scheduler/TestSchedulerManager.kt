package com.challengefy.feature.base.scheduler

import com.challengefy.base.scheduler.SchedulerManager
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TestSchedulerManager @Inject constructor() : SchedulerManager {
    override fun mainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun ioThread(): Scheduler {
        return Schedulers.trampoline()
    }
}
