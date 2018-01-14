package com.challengefy.estimate.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.challengefy.data.model.Address
import com.challengefy.databinding.FragmentDestinationBinding
import com.challengefy.estimate.activity.HomeActivity

class DestinationFragment : Fragment() {

    private lateinit var binding: FragmentDestinationBinding

    companion object {
        fun newInstance() = DestinationFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDestinationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.estimateCardDestination.setOnClickListener {
            (activity as HomeActivity).goToDestination(binding.estimateCardDestination)
        }
    }

    fun showDestination(address: Address) {
        binding.destination = address
    }

    fun showPickup(address: Address) {
        binding.pickup = address
    }
}
