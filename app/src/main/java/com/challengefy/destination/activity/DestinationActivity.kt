package com.challengefy.destination.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.transition.ChangeBounds
import android.support.transition.TransitionManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.EditText
import com.challengefy.R
import com.challengefy.base.activity.BaseActivity
import com.challengefy.data.model.Address
import com.challengefy.data.model.PredictionAddress
import com.challengefy.destination.adapter.DestinationAdapter
import com.challengefy.destination.viewmodel.DestinationViewModel
import com.challengefy.util.bind
import com.jakewharton.rxbinding2.widget.textChangeEvents
import timber.log.Timber
import javax.inject.Inject

class DestinationActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: DestinationViewModel

    companion object {
        fun startIntent(context: Context) = Intent(context, DestinationActivity::class.java)
    }

    private val ctnRoot by bind<ViewGroup>(R.id.destination_ctn_root)
    private val listDestination by bind<RecyclerView>(R.id.destination_list)
    private val inputAddress by bind<EditText>(R.id.destination_inp_address)
    private val adapter = DestinationAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination)
        initViews()
    }

    private fun initViews() {
        listDestination.layoutManager = LinearLayoutManager(this)
        listDestination.adapter = adapter

        bindTextChangeEvents()
        bindAddressPredictions()
        bindAddressClick()
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        animateEnter()
    }

    private fun bindAddressClick() {
        adapter.setOnItemClick { detailPrediction(it) }
    }

    private fun detailPrediction(it: PredictionAddress) {
        viewModel.detailPrediction(it)
                .subscribe(
                        { finishWithResult(it) },
                        { Timber.d(it) }

                )
    }

    private fun finishWithResult(address: Address) {
        setResult(RESULT_OK, Intent())
        supportFinishAfterTransition()
    }

    private fun bindAddressPredictions() {
        viewModel.addresses()
                .subscribe(
                        { showItems(it) },
                        { Timber.d(it) }
                )
    }

    private fun showItems(it: List<PredictionAddress>) {
        TransitionManager.beginDelayedTransition(ctnRoot, ChangeBounds()
                .addTarget(listDestination)
        )
        adapter.setItems(it)
    }

    private fun bindTextChangeEvents() {
        inputAddress.textChangeEvents()
                .skip(1)
                .map { it.text().toString() }
                .subscribe(
                        { viewModel.inputSearch(it) },
                        { Timber.d(it) }
                )
    }

    private fun animateEnter() {
        inputAddress.visibility = View.VISIBLE

        ViewAnimationUtils.createCircularReveal(inputAddress, inputAddress.left, inputAddress.top, 0f, inputAddress.width.toFloat())
                .apply {
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            inputAddress.requestFocus()
                        }
                    })
                    start()
                }
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

}
