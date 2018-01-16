package com.challengefy.feature.estimate.viewmodel

import android.content.Intent
import android.databinding.ObservableField
import com.challengefy.base.di.scope.FragmentScope
import com.challengefy.data.model.Address
import com.challengefy.feature.address.activity.AddressSearchActivity
import com.challengefy.feature.estimate.navigator.HomeNavigator
import javax.inject.Inject

@FragmentScope
class DestinationViewModel @Inject constructor(
        private val homeViewModel: HomeViewModel,
        private val homeNavigator: HomeNavigator
) : HomeNavigator.ResolutionListener {

    val destinationAddress: ObservableField<Address> = ObservableField()

    fun init() {
        homeNavigator.attachResultListener(this)
    }

    fun dispose() {
        homeNavigator.detachResultListener(this)
    }

    fun onDestinationClick() {
        homeNavigator.goToAddressSearch(destinationAddress.get())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            HomeNavigator.REQUEST_CODE_ADDRESS_SEARCH -> {
                val address = data?.getParcelableExtra<Address>(AddressSearchActivity.RESULT_ADDRESS)
                if (address != null) {
                    locationReceived(address)
                }
            }
        }
    }

    private fun locationReceived(address: Address) {
        destinationAddress.set(address)
        homeViewModel.destinationReceived(address)
    }

    override fun onPermissionResult(requestCode: Int, granted: Boolean) {
        // Do Nothing
    }
}
