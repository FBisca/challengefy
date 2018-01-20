package com.challengefy.feature.ride.bindings

import android.graphics.Rect
import com.challengefy.base.di.scope.ActivityScope
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@ActivityScope
class MapPaddingBinding @Inject constructor() {

    private val emptyRect = Rect(0, 0, 0, 0)
    private val paddingObservable = BehaviorSubject.createDefault(emptyRect)

    fun paddingChangeEvents(): Observable<Rect> {
        return paddingObservable
    }

    fun postPaddingChange(rect: Rect) {
        paddingObservable.onNext(rect)
    }
}
