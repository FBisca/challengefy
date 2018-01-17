package com.challengefy.feature.estimate.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.challengefy.databinding.FragmentConfirmPickupBinding
import com.challengefy.feature.estimate.viewmodel.ConfirmPickupViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ConfirmPickupFragment : Fragment() {

    companion object {
        fun newInstance() = ConfirmPickupFragment()
    }

    @Inject
    lateinit var viewModel: ConfirmPickupViewModel

    private lateinit var binding: FragmentConfirmPickupBinding

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
        binding.viewModel = viewModel
    }
}