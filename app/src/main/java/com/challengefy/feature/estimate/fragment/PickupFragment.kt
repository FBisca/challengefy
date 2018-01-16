package com.challengefy.feature.estimate.fragment

import android.content.Context
import android.databinding.Observable
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import com.challengefy.R
import com.challengefy.databinding.FragmentFindingLocationBinding
import com.challengefy.feature.estimate.viewmodel.PickupViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PickupFragment : Fragment() {

    @Inject
    lateinit var viewModel: PickupViewModel

    lateinit var binding: FragmentFindingLocationBinding

    private val viewStateListener = ViewStateChangeListener()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentFindingLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.viewState.addOnPropertyChangedCallback(viewStateListener)
        viewModel.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.viewState.removeOnPropertyChangedCallback(viewStateListener)
        viewModel.dispose()
    }

    private fun showNoPermission() {
        binding.apply {
            TransitionManager.beginDelayedTransition(root as ViewGroup)

            pickupCard.visibility = View.VISIBLE
            pickupCard.layoutParams.width = LayoutParams.MATCH_PARENT

            pickupLoading.visibility = View.GONE
            pickupAddress.visibility = View.VISIBLE
            pickupCardLocationPermission.visibility = View.VISIBLE

            pickupTxtLocationTitle.setText(R.string.location_permission_denied)
            pickupTxtLocationDescription.setText(R.string.location_permission_denied_description)
        }
    }

    private fun showLocationDisabled() {
        binding.apply {
            TransitionManager.beginDelayedTransition(root as ViewGroup)

            pickupCard.visibility = View.VISIBLE
            pickupCard.layoutParams.width = LayoutParams.MATCH_PARENT

            pickupLoading.visibility = View.GONE
            pickupAddress.visibility = View.VISIBLE
            pickupCardLocationPermission.visibility = View.VISIBLE

            pickupTxtLocationTitle.setText(R.string.location_permission_denied)
            pickupTxtLocationDescription.setText(R.string.location_permission_denied_description)
        }
    }

    private fun showLoading() {
        binding.apply {
            TransitionManager.beginDelayedTransition(root as ViewGroup)

            pickupCard.visibility = View.VISIBLE
            pickupCard.layoutParams.width = LayoutParams.WRAP_CONTENT

            pickupLoading.visibility = View.VISIBLE
            pickupAddress.visibility = View.GONE
            pickupCardLocationPermission.visibility = View.GONE
        }
    }

    private fun showLocationReceived() {
        binding.apply {
            TransitionManager.beginDelayedTransition(root as ViewGroup)

            pickupCard.visibility = View.VISIBLE
            pickupCard.layoutParams.width = LayoutParams.MATCH_PARENT

            pickupLoading.visibility = View.GONE
            pickupAddress.visibility = View.VISIBLE
            pickupCardLocationPermission.visibility = View.GONE
        }
    }

    inner class ViewStateChangeListener : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable, propertyId: Int) {
            val viewState = viewModel.viewState.get()
            when (viewState) {
                PickupViewModel.ViewState.LOCATION_NO_PERMISSION -> showNoPermission()
                PickupViewModel.ViewState.LOCATION_DISABLED -> showLocationDisabled()
                PickupViewModel.ViewState.LOADING -> showLoading()
                PickupViewModel.ViewState.LOCATION_RECEIVED -> showLocationReceived()
                else -> Unit // Do nothing
            }
        }

    }
}
