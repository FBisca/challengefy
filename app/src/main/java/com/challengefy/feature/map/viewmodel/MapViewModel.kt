package com.challengefy.feature.map.viewmodel

import android.databinding.Observable
import android.databinding.ObservableField
import com.challengefy.base.di.scope.FragmentScope
import com.challengefy.feature.estimate.viewmodel.HomeViewModel
import javax.inject.Inject

@FragmentScope
class MapViewModel @Inject constructor(
        homeViewModel: HomeViewModel
) {
    val pickUpAddress = homeViewModel.pickUpAddress
    val destinationAddress = homeViewModel.destinationAddress
    val viewState = ObservableField<ViewState>(ViewState.IDLE)

    private val homeViewState = homeViewModel.viewState

    private val homeViewStateListener = HomeViewStateChangeListener()

    fun init() {
        homeViewState.addOnPropertyChangedCallback(homeViewStateListener)
        pickUpAddress.addOnPropertyChangedCallback(homeViewStateListener)
        destinationAddress.addOnPropertyChangedCallback(homeViewStateListener)
    }

    fun dispose() {
        homeViewState.removeOnPropertyChangedCallback(homeViewStateListener)
        pickUpAddress.removeOnPropertyChangedCallback(homeViewStateListener)
        destinationAddress.removeOnPropertyChangedCallback(homeViewStateListener)
    }

    inner class HomeViewStateChangeListener : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            val state = homeViewState.get()
            val newState = when {
                state == HomeViewModel.ViewState.PICKUP && pickUpAddress.get() != null -> ViewState.SHOW_PICKUP
                state == HomeViewModel.ViewState.DESTINATION && destinationAddress.get() != null -> ViewState.SHOW_DESTINATION
                else -> viewState.get()
            }

            if (newState == viewState.get()) {
                viewState.notifyChange()
            } else {
                viewState.set(newState)
            }
        }
    }

    enum class ViewState {
        IDLE, SHOW_PICKUP, SHOW_DESTINATION
    }
}