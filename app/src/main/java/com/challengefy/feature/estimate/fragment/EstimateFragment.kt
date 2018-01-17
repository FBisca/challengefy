package com.challengefy.feature.estimate.fragment

import android.content.Context
import android.databinding.Observable
import android.os.Bundle
import android.support.transition.AutoTransition
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.HORIZONTAL
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.challengefy.databinding.FragmentEstimateBinding
import com.challengefy.feature.estimate.adapter.EstimateAdapter
import com.challengefy.feature.estimate.adapter.EstimateMarginDecoration
import com.challengefy.feature.estimate.viewmodel.EstimateViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class EstimateFragment : Fragment() {

    companion object {
        fun newInstance() = EstimateFragment()
    }

    @Inject
    lateinit var viewModel: EstimateViewModel

    private lateinit var binding: FragmentEstimateBinding
    private val adapter = EstimateAdapter()
    private val viewStateListener = ViewStateChangeListener()

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
        viewModel.viewState.addOnPropertyChangedCallback(viewStateListener)

        initView()

        binding.viewModel = viewModel
        viewModel.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.viewState.removeOnPropertyChangedCallback(viewStateListener)
        viewModel.dispose()
    }

    private fun initView() {
        val estimateList = binding.estimateList

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(estimateList)
        estimateList.layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        estimateList.adapter = adapter
        estimateList.addItemDecoration(EstimateMarginDecoration(estimateList.context))
        estimateList.addOnScrollListener(ScrollListener())
    }

    private fun showLoading() {
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, createTransition())

        binding.estimateListGroup.visibility = View.GONE
        binding.estimateLoading.visibility = View.VISIBLE
        binding.estimateErrorGroup.visibility = View.GONE
    }

    private fun showEstimates() {
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, createTransition())

        binding.estimateLoading.visibility = View.GONE
        binding.estimateListGroup.visibility = View.VISIBLE
        binding.estimateErrorGroup.visibility = View.GONE
    }

    private fun showError() {
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, createTransition())

        binding.estimateListGroup.visibility = View.GONE
        binding.estimateLoading.visibility = View.GONE
        binding.estimateErrorGroup.visibility = View.VISIBLE
    }

    private fun createTransition() = AutoTransition().apply {
            addTarget(binding.estimateCardContainer)
            addTarget(binding.estimateLoading)
            addTarget(binding.estimateList)
            addTarget(binding.estimateTxtCarTitle)
            addTarget(binding.estimateBtnRequest)
            addTarget(binding.estimateErrorBg)
            addTarget(binding.estimateError)
            addTarget(binding.estimateBtnTryAgain)
            addTarget(binding.estimateTxtError)
    }

    inner class ViewStateChangeListener : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            val viewState = viewModel.viewState.get()
            when (viewState) {
                EstimateViewModel.ViewState.LOADING -> showLoading()
                EstimateViewModel.ViewState.ESTIMATE_LOADED -> showEstimates()
                EstimateViewModel.ViewState.ERROR -> showError()
                else -> Unit
            }
        }

    }

    inner class ScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val view = recyclerView.findChildViewUnder(recyclerView.width / 2f, recyclerView.height / 2f)
            if (view != null) {
                val selectedPos = recyclerView.getChildAdapterPosition(view)
                viewModel.itemSelected(selectedPos)
                for (index in 0 until recyclerView.adapter.itemCount) {
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as EstimateAdapter.ViewHolder?
                    viewHolder?.let {
                        it.binding.selected = index == selectedPos
                        it.binding.executePendingBindings()
                    }
                }
            }
        }
    }
}
