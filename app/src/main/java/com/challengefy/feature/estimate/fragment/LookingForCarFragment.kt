package com.challengefy.feature.estimate.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.challengefy.databinding.FragmentLookingForCarBinding
import com.challengefy.feature.estimate.viewmodel.LookingForCarViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class LookingForCarFragment : Fragment() {

    companion object {
        fun newInstance() = LookingForCarFragment()
    }

    @Inject
    lateinit var viewModel: LookingForCarViewModel

    private lateinit var binding: FragmentLookingForCarBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentLookingForCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
    }
}