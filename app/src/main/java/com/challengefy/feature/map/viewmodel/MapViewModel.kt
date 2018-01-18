package com.challengefy.feature.map.viewmodel

import android.databinding.Observable
import android.databinding.ObservableField
import com.challengefy.base.di.scope.FragmentScope
import com.challengefy.base.scheduler.SchedulerManager
import com.challengefy.feature.estimate.viewmodel.HomeViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.disposables.Disposables
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@FragmentScope
class MapViewModel @Inject constructor(
        homeViewModel: HomeViewModel,
        private val schedulerManager: SchedulerManager
) {
    val pickUpAddress = homeViewModel.pickUpAddress
    val destinationAddress = homeViewModel.destinationAddress
    val viewState = ObservableField<ViewState>(ViewState.IDLE)

    private val homeViewState = homeViewModel.viewState
    private var disposable = Disposables.empty()

    fun init() {
        disposable = homeViewStateChanges()
                .throttleLast(300, TimeUnit.MILLISECONDS)
                .observeOn(schedulerManager.mainThread())
                .subscribe(
                        { newState ->
                            if (newState == viewState.get()) {
                                viewState.notifyChange()
                            } else {
                                viewState.set(newState)
                            }
                        },
                        {
                            Timber.e(it)
                        }
                )
    }

    fun dispose() {
        disposable.dispose()
    }

    private fun homeViewStateChanges(): Flowable<ViewState> {
        return Flowable.create({ emitter ->
            val homeViewStateListener = HomeViewStateChangeListener(emitter)
            homeViewState.addOnPropertyChangedCallback(homeViewStateListener)
            pickUpAddress.addOnPropertyChangedCallback(homeViewStateListener)
            destinationAddress.addOnPropertyChangedCallback(homeViewStateListener)

            emitter.setCancellable {
                homeViewState.removeOnPropertyChangedCallback(homeViewStateListener)
                pickUpAddress.removeOnPropertyChangedCallback(homeViewStateListener)
                destinationAddress.removeOnPropertyChangedCallback(homeViewStateListener)
            }

        }, BackpressureStrategy.LATEST)
    }

    enum class ViewState {
        IDLE, SHOW_PICKUP, SHOW_DESTINATION
    }

    inner class HomeViewStateChangeListener(
            private val emitter: FlowableEmitter<ViewState>
    ) : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            val state = homeViewState.get()
            val newState = when {
                state == HomeViewModel.ViewState.PICKUP && pickUpAddress.get() != null -> ViewState.SHOW_PICKUP
                state == HomeViewModel.ViewState.DESTINATION && destinationAddress.get() != null -> ViewState.SHOW_DESTINATION
                state == HomeViewModel.ViewState.CONFIRM_PICKUP -> ViewState.SHOW_PICKUP
                state == HomeViewModel.ViewState.ESTIMATE -> ViewState.SHOW_DESTINATION
                else -> viewState.get()
            }

            emitter.onNext(newState)
        }
    }
}
