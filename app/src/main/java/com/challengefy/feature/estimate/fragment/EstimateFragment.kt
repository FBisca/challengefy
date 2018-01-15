package com.challengefy.feature.estimate.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.*
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.challengefy.data.model.Address
import com.challengefy.data.model.Estimate
import com.challengefy.databinding.FragmentEstimateBinding
import com.challengefy.feature.estimate.adapter.EstimateAdapter
import com.challengefy.feature.estimate.viewmodel.EstimateViewModel
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber
import javax.inject.Inject

class EstimateFragment : Fragment() {

    @Inject
    lateinit var viewModel: EstimateViewModel

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

    private val adapter = EstimateAdapter()
    private lateinit var binding: FragmentEstimateBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentEstimateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        bindEstimates()
    }

    private fun initView() {
        binding.pickup = getPickupArgument()
        binding.destination = getDestinationArgument()

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.estimateList)
        binding.estimateList.layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        binding.estimateList.adapter = adapter

        binding.estimateList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val viewSize = (resources.displayMetrics.density * 130).toInt()
                val pos = parent.getChildAdapterPosition(view)
                val itemCount = state.itemCount
                if (pos == 0) {
                    outRect.left = (parent.width / 2) - (viewSize / 2)
                }

                if (pos == itemCount - 1) {
                    outRect.right = (parent.width / 2) - (viewSize / 2)
                }
            }
        })
    }

    private fun bindEstimates() {
        viewModel.estimate()
                .doOnSubscribe { binding.estimateLoading.show() }
                .doOnDispose { binding.estimateLoading.hide() }
                .subscribe(
                        { bindItems(it) },
                        { Timber.d(it) }
                )
    }

    private fun bindItems(estimates: List<Estimate>) {
        adapter.setItems(estimates)
    }

    fun getDestinationArgument(): Address {
        return arguments?.getParcelable(EXTRA_DESTINATION) ?: throw IllegalArgumentException("Destination argument required")
    }

    fun getPickupArgument(): Address {
        return arguments?.getParcelable(EXTRA_PICKUP) ?: throw IllegalArgumentException("Destination argument required")
    }

}
