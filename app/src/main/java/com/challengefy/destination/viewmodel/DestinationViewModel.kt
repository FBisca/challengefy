package com.challengefy.destination.viewmodel

import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.data.model.Address
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@ActivityScope
class DestinationViewModel @Inject constructor() {

    private val inputSubject = PublishSubject.create<String>()

    fun addresses(): Observable<List<Address>> {
        TODO()
    }

    fun inputSearch(text: String) {
        inputSubject.onNext(text)
    }

}
