package com.challengefy.feature.address.activity

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.transition.AutoTransition
import android.support.transition.TransitionManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.challengefy.R
import com.challengefy.base.activity.BaseActivity
import com.challengefy.data.model.Address
import com.challengefy.data.model.PredictionAddress
import com.challengefy.databinding.ActivityAddressSearchBinding
import com.challengefy.feature.address.adapter.AddressSearchAdapter
import com.challengefy.feature.address.viewmodel.AddressSearchViewModel
import com.jakewharton.rxbinding2.widget.editorActionEvents
import com.jakewharton.rxbinding2.widget.textChangeEvents
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class AddressSearchActivity : BaseActivity() {

    companion object {
        private const val EXTRA_ADDRESS = "EXTRA_ADDRESS"
        const val RESULT_ADDRESS = "RESULT_ADDRESS"

        fun startIntent(context: Context, address: Address?): Intent {
            return Intent(context, AddressSearchActivity::class.java)
                    .putExtra(EXTRA_ADDRESS, address)
        }
    }

    @Inject
    lateinit var viewModel: AddressSearchViewModel

    private lateinit var binding: ActivityAddressSearchBinding

    private val adapter = AddressSearchAdapter()

    private val disposable = CompositeDisposable()

    private val viewStateChangeListener = ViewStateChangeListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_search)
        initViews()

        binding.viewModel = viewModel
        viewModel.viewState.addOnPropertyChangedCallback(viewStateChangeListener)
        viewModel.init()
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.viewState.removeOnPropertyChangedCallback(viewStateChangeListener)
        disposable.dispose()
        viewModel.dispose()
    }

    private fun initViews() {
        binding.addressSearchList.layoutManager = LinearLayoutManager(this)
        binding.addressSearchList.adapter = adapter

        val address = intent.getParcelableExtra<Address>(EXTRA_ADDRESS)
        if (address != null) {
            binding.addressSearchInpAddress.setText(address.title)
            binding.addressSearchInpAddress.setSelection(address.title.length)
        }

        bindTextChangeEvents()
        bindOnEditorEvents()
        bindAddressClick()
    }

    private fun bindOnEditorEvents() {
        binding.addressSearchInpAddress.editorActionEvents()
                .filter { it.actionId() == EditorInfo.IME_ACTION_DONE }
                .subscribe(
                        { viewModel.onKeyDoneTapped() },
                        { Timber.d(it) }
                )
                .addToDisposable()
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


    private fun bindTextChangeEvents() {
        binding.addressSearchInpAddress.textChangeEvents()
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

    private fun showLoading() {
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, AutoTransition()
                .addTarget(binding.addressSearchLoading)
        )

        binding.addressSearchLoading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, AutoTransition()
                .addTarget(binding.addressSearchLoading)
        )

        binding.addressSearchLoading.visibility = View.GONE
    }

    private fun Disposable.addToDisposable() {
        disposable.add(this)
    }

    inner class ViewStateChangeListener : android.databinding.Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: android.databinding.Observable?, propertyId: Int) {
            if (viewModel.viewState.get() == AddressSearchViewModel.ViewState.LOADING) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

}
