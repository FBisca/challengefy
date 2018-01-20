package com.challengefy.feature.ride.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.challengefy.base.util.boundsChangeEvents
import com.challengefy.databinding.FragmentDestinationBinding
import com.challengefy.feature.ride.bindings.MapPaddingBinding
import com.challengefy.feature.ride.viewmodel.DestinationViewModel
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.Disposables
import timber.log.Timber
import javax.inject.Inject

class DestinationFragment : Fragment() {

    companion object {
        fun newInstance() = DestinationFragment()
    }

    @Inject
    lateinit var viewModel: DestinationViewModel

    @Inject
    lateinit var mapPaddingBinding: MapPaddingBinding

    private lateinit var binding: FragmentDestinationBinding

    private var paddingDisposable = Disposables.empty()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDestinationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindPaddingChange()
        binding.viewModel = viewModel

        viewModel.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        paddingDisposable.dispose()
        viewModel.dispose()
    }

    private fun bindPaddingChange() {
        val view = binding.estimateCardPickup?.estimateCardPickup
        if (view != null) {
            paddingDisposable = view.boundsChangeEvents()
                    .map { Rect(0, 0, 0, binding.root.height - it.top) }
                    .subscribe(
                            { mapPaddingBinding.postPaddingChange(it) },
                            { Timber.e(it) }
                    )
        }
    }

}
