package com.challengefy.estimate.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.challengefy.R
import com.challengefy.estimate.activity.EstimateActivity
import com.challengefy.util.bind

class AddressesFragment : Fragment() {

    private val cardDestination by bind<CardView>(R.id.estimate_card_destination)

    companion object {
        fun newInstance() = AddressesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_addresses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardDestination.setOnClickListener {
            (activity as EstimateActivity).goToDestination(cardDestination)
        }
    }
}
