package com.challengefy.feature.estimate.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.challengefy.data.model.Address
import com.challengefy.databinding.FragmentEstimateBinding

class EstimateFragment : Fragment() {

    companion object {
        private const val EXTRA_DESTINATION = "EXTRA_DESTINATION"
        private const val EXTRA_PICKUP = "EXTRA_PICKUP"

        fun newInstance(pickup: Address, destination: Address) = EstimateFragment().apply {
            val args = Bundle()
            args.putParcelable(EXTRA_DESTINATION, destination)
            args.putParcelable(EXTRA_PICKUP, pickup)
            arguments = args
        }
    }

    private lateinit var binding: FragmentEstimateBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentEstimateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pickup = getPickupArgument()
        binding.destination = getDestinationArgument()
    }

    private fun getDestinationArgument(): Address {
        return arguments?.getParcelable(EXTRA_DESTINATION) ?: throw IllegalArgumentException("Destination argument required")
    }

    private fun getPickupArgument(): Address {
        return arguments?.getParcelable(EXTRA_PICKUP) ?: throw IllegalArgumentException("Destination argument required")
    }

}
