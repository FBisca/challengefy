package com.challengefy.feature.estimate.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.challengefy.base.util.boundsChangeEvents
import com.challengefy.databinding.FragmentConfirmPickupBinding
import com.challengefy.feature.estimate.bindings.MapPaddingBinding
import com.challengefy.feature.estimate.viewmodel.ConfirmPickupViewModel
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.Disposables
import timber.log.Timber
import javax.inject.Inject

class ConfirmPickupFragment : Fragment() {

    companion object {
        fun newInstance() = ConfirmPickupFragment()
    }

    @Inject
    lateinit var viewModel: ConfirmPickupViewModel

    @Inject
    lateinit var mapPaddingBinding: MapPaddingBinding

    private lateinit var binding: FragmentConfirmPickupBinding

    private var paddingDisposable = Disposables.empty()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentConfirmPickupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindPaddingChange()
        binding.viewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        paddingDisposable.dispose()
    }

    private fun bindPaddingChange() {
        paddingDisposable = binding.estimateCardContainer.boundsChangeEvents()
                .map { Rect(it.left, 0, binding.root.width - it.right, binding.root.height - it.bottom) }
                .subscribe(
                        { mapPaddingBinding.postPaddingChange(it) },
                        { Timber.e(it) }
                )
    }
}
