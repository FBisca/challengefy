package com.challengefy.feature.estimate.viewmodel

import android.databinding.Observable
import android.databinding.ObservableField
import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.data.model.Address
import com.challengefy.data.model.Estimate
import com.challengefy.data.repository.PositionRepository
import javax.inject.Inject

@ActivityScope
class HomeViewModel @Inject constructor(
        private val positionRepository: PositionRepository
) {

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

    fun location() = positionRepository.getCurrentPosition()

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

    }

    private fun updateViewState(newState: ViewState) {
        if (newState != viewState.get()) {
            viewState.set(newState)
        } else {
            viewState.notifyChange()
        }
    }

    enum class ViewState {
        PICKUP, DESTINATION, ESTIMATE, CONFIRM_PICKUP, FINDING_CAR
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
