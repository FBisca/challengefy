package com.challengefy.feature.estimate.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.challengefy.data.model.Address
import com.challengefy.databinding.FragmentDestinationBinding
import com.challengefy.feature.estimate.viewmodel.DestinationViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class DestinationFragment : Fragment() {

    companion object {
        const val EXTRA_PICKUP_ADDRESS = "EXTRA_PICKUP_ADDRESS"

        fun newInstance(pickup: Address) = DestinationFragment().apply {
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PICKUP_ADDRESS, pickup)
            arguments = bundle
        }
    }

    @Inject
    lateinit var viewModel: DestinationViewModel

    private lateinit var binding: FragmentDestinationBinding

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
        binding.pickup = getPickUpAddress()
        binding.viewModel = viewModel

        viewModel.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.dispose()
    }

    fun getPickUpAddress() = arguments?.getParcelable<Address>(EXTRA_PICKUP_ADDRESS)

}
