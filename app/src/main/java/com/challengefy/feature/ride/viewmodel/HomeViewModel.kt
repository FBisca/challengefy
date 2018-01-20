package com.challengefy.feature.ride.viewmodel

import android.databinding.Observable
import android.databinding.ObservableField
import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.data.model.Address
import com.challengefy.data.model.Estimate
import javax.inject.Inject

@ActivityScope
class HomeViewModel @Inject constructor() {

    val viewState: ObservableField<ViewState> = ObservableField(ViewState.PICKUP)

    val pickUpAddress = ObservableField<Address>()
    val destinationAddress = ObservableField<Address>()
    val estimateSelected = ObservableField<Estimate>()
    val estimates = ObservableField<List<Estimate>>()

    private val addressChangeListener = AddressChangeListener()

    fun init() {
        pickUpAddress.addOnPropertyChangedCallback(addressChangeListener)
        destinationAddress.addOnPropertyChangedCallback(addressChangeListener)
    }

    fun dispose() {
        pickUpAddress.removeOnPropertyChangedCallback(addressChangeListener)
        destinationAddress.removeOnPropertyChangedCallback(addressChangeListener)
    }

    fun onBack() {
        val state = viewState.get()
        state?.previous?.let {
            updateViewState(it)
        }
    }

    fun pickUpReceived() {
        updateViewState(ViewState.DESTINATION)
    }

    fun destinationReceived() {
        updateViewState(ViewState.ESTIMATE)
    }

    fun estimatedReceived() {
        updateViewState(ViewState.CONFIRM_PICKUP)
    }

    fun pickUpConfirmed() {
        updateViewState(ViewState.LOOKING_FOR_CAR)
    }

    private fun updateViewState(newState: ViewState) {
        if (newState != viewState.get()) {
            viewState.set(newState)
        } else {
            viewState.notifyChange()
        }
    }

    enum class ViewState(
            val previous: ViewState?
    ) {
        PICKUP(null),
        DESTINATION(previous = PICKUP),
        ESTIMATE(previous = DESTINATION),
        CONFIRM_PICKUP(previous = ESTIMATE),
        LOOKING_FOR_CAR(previous = CONFIRM_PICKUP)
    }

    inner class AddressChangeListener : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            val value = estimates.get()
            if (value == null) {
                estimates.notifyChange()
            }

            estimates.set(null)
        }
    }
}
