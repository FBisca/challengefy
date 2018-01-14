package com.challengefy.estimate.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.challengefy.R
import com.challengefy.data.model.Address
import com.challengefy.databinding.FragmentAddressesBinding
import com.challengefy.estimate.activity.EstimateActivity
import com.challengefy.util.bind

class AddressesFragment : Fragment() {

    private val cardDestination by bind<CardView>(R.id.estimate_card_destination)

    private lateinit var binding: FragmentAddressesBinding

    companion object {
        fun newInstance() = AddressesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAddressesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardDestination.setOnClickListener {
            (activity as EstimateActivity).goToDestination(cardDestination)
        }
    }

    fun showDestination(address: Address) {
        binding.destination = address
    }

    fun showPickup(address: Address) {
        binding.pickup = address
    }
}
