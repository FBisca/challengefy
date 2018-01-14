package com.challengefy.destination.activity

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.transition.ChangeBounds
import android.support.transition.TransitionManager
import android.support.v7.widget.LinearLayoutManager
import com.challengefy.R
import com.challengefy.base.activity.BaseActivity
import com.challengefy.data.model.Address
import com.challengefy.data.model.PredictionAddress
import com.challengefy.databinding.ActivityAddressSearchBinding
import com.challengefy.destination.adapter.DestinationAdapter
import com.challengefy.destination.viewmodel.DestinationViewModel
import com.jakewharton.rxbinding2.widget.textChangeEvents
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class AddressSearchActivity : BaseActivity() {

    companion object {
        private const val EXTRA_ADDRESS = "EXTRA_ADDRESS"
        const val RESULT_ADDRESS = "RESULT_ADDRESS"

        fun startIntent(context: Context, address: Address?) = Intent(context, AddressSearchActivity::class.java)
                .putExtra(EXTRA_ADDRESS, address)
    }

    @Inject
    lateinit var viewModel: DestinationViewModel

    private val adapter = DestinationAdapter()

    private val disposable = CompositeDisposable()

    private lateinit var binding: ActivityAddressSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_search)
        initViews()
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun initViews() {
        binding.addressSearchList.layoutManager = LinearLayoutManager(this)
        binding.addressSearchList.adapter = adapter

        val address = getInitialAddress()
        if (address != null) {
            binding.addressSearchInpAddress.setText(address.title)
            binding.addressSearchInpAddress.setSelection(address.title.length)
        }

        bindTextChangeEvents()
        bindAddressPredictions()
        bindAddressClick()
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
                .addToDisposable()
    }

    private fun bindAddressPredictions() {
        viewModel.addresses()
                .subscribe(
                        { showItems(it) },
                        { Timber.d(it) }
                )
                .addToDisposable()
    }

    private fun bindTextChangeEvents() {
        binding.addressSearchInpAddress.textChangeEvents()
                .skip(1)
                .map { it.text().toString() }
                .subscribe(
                        { viewModel.inputSearch(it) },
                        { Timber.d(it) }
                )
                .addToDisposable()
    }

    private fun finishWithResult(address: Address) {
        setResult(RESULT_OK, Intent().putExtra(RESULT_ADDRESS, address))
        supportFinishAfterTransition()
    }

    private fun showItems(it: List<PredictionAddress>) {
        TransitionManager.beginDelayedTransition(binding.addressSearchCtnRoot, ChangeBounds()
                .addTarget(binding.addressSearchList)
        )
        adapter.setItems(it)
    }

    private fun getInitialAddress(): Address? {
        return intent.getParcelableExtra(EXTRA_ADDRESS)
    }

    private fun Disposable.addToDisposable() {
        disposable.add(this)
    }

}