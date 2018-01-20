package com.challengefy.feature.ride.fragment

import android.content.Context
import android.databinding.Observable
import android.graphics.Rect
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import com.challengefy.R
import com.challengefy.base.util.boundsChangeEvents
import com.challengefy.databinding.FragmentPickupBinding
import com.challengefy.feature.ride.bindings.MapPaddingBinding
import com.challengefy.feature.ride.viewmodel.PickupViewModel
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.Disposables
import timber.log.Timber
import javax.inject.Inject

class PickupFragment : Fragment() {

    companion object {
        fun newInstance() = PickupFragment()
    }

    @Inject
    lateinit var viewModel: PickupViewModel

    @Inject
    lateinit var mapPaddingBinding: MapPaddingBinding

    lateinit var binding: FragmentPickupBinding
    private val viewStateListener = ViewStateChangeListener()
    private var paddingDisposable = Disposables.empty()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPickupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindPaddingChange()
        binding.viewModel = viewModel

        viewModel.viewState.addOnPropertyChangedCallback(viewStateListener)
        viewModel.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        paddingDisposable.dispose()
        viewModel.viewState.removeOnPropertyChangedCallback(viewStateListener)
        viewModel.dispose()
    }

    private fun showNoPermission() {
        binding.apply {
            TransitionManager.beginDelayedTransition(root as ViewGroup)

            pickupCard.visibility = View.VISIBLE
            pickupCard.layoutParams.width = LayoutParams.MATCH_PARENT

            estimateBtnConfirm.visibility = View.GONE
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

            estimateBtnConfirm.visibility = View.GONE
            pickupLoading.visibility = View.GONE
            pickupAddress.visibility = View.VISIBLE
            pickupCardLocationPermission.visibility = View.VISIBLE

            pickupTxtLocationTitle.setText(R.string.location_disabled)
            pickupTxtLocationDescription.setText(R.string.location_disabled_description)
        }
    }

    private fun showLoading() {
        binding.apply {
            TransitionManager.beginDelayedTransition(root as ViewGroup)

            pickupCard.visibility = View.VISIBLE
            pickupCard.layoutParams.width = LayoutParams.WRAP_CONTENT

            estimateBtnConfirm.visibility = View.GONE
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

            estimateBtnConfirm.visibility = View.VISIBLE
            pickupLoading.visibility = View.GONE
            pickupAddress.visibility = View.VISIBLE
            pickupCardLocationPermission.visibility = View.GONE
        }
    }

    private fun bindPaddingChange() {
        paddingDisposable = binding.pickupCard.boundsChangeEvents()
                .map { rect -> Rect(0, 0, 0, binding.root.height - rect.top) }
                .subscribe(
                        { mapPaddingBinding.postPaddingChange(it) },
                        { Timber.e(it) }
                )
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
