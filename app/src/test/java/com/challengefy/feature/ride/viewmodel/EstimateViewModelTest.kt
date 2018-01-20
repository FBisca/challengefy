package com.challengefy.feature.ride.viewmodel

import android.databinding.ObservableField
import com.challengefy.data.model.Address
import com.challengefy.data.model.Estimate
import com.challengefy.data.repository.RideRepository
import com.challengefy.feature.ride.navigator.HomeNavigator
import com.challengefy.test.Fabricator
import com.challengefy.test.TestSchedulerManager
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EstimateViewModelTest {

    @Mock
    lateinit var navigator: HomeNavigator

    @Mock
    lateinit var homeViewModel: HomeViewModel

    @Mock
    lateinit var rideRepository: RideRepository

    val pickUpAddress = ObservableField<Address>()
    val destinationAddress = ObservableField<Address>()
    val estimateSelected = ObservableField<Estimate>()
    val estimates = ObservableField<List<Estimate>>()

    lateinit var viewModel: EstimateViewModel

    @Before
    fun setUp() {
        `when`(homeViewModel.pickUpAddress).thenReturn(pickUpAddress)
        `when`(homeViewModel.destinationAddress).thenReturn(destinationAddress)
        `when`(homeViewModel.estimateSelected).thenReturn(estimateSelected)
        `when`(homeViewModel.estimates).thenReturn(estimates)

        viewModel = EstimateViewModel(navigator, homeViewModel, rideRepository, TestSchedulerManager())
    }

    @Test
    fun testOnInitShouldRequestEstimates() {
        val pickup = Fabricator.address()
        val destination = Fabricator.address()
        val estimate = Fabricator.estimate()

        `when`(rideRepository.estimateRide(pickup, destination)).thenReturn(Single.just(listOf(estimate)))

        pickUpAddress.set(Fabricator.address())
        destinationAddress.set(Fabricator.address())

        viewModel.init()

        verify(rideRepository).estimateRide(pickup, destination)
    }

    @Test
    fun testConfirmShouldWorkProperty() {
        val estimate = Fabricator.estimate()
        val estimates = listOf(estimate, estimate)

        viewModel.itemSelected.set(1)
        viewModel.estimates.set(estimates)

        viewModel.onConfirmClick()

        verify(homeViewModel).estimatedReceived()
    }
}
