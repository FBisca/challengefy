package com.challengefy.feature.ride.viewmodel

import com.challengefy.feature.ride.navigator.HomeNavigator
import com.challengefy.feature.ride.viewmodel.ConfirmPickupViewModel
import com.challengefy.feature.ride.viewmodel.DestinationViewModel
import com.challengefy.feature.ride.viewmodel.HomeViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DestinationViewModelTest {

    @Mock
    lateinit var navigator: HomeNavigator

    @Mock
    lateinit var homeViewModel: HomeViewModel

    lateinit var viewModel: DestinationViewModel

    @Before
    fun setUp() {
        viewModel = DestinationViewModel(
                navigator,
                homeViewModel
        )
    }

    @Test
    fun testConfirmDestination() {
        viewModel.onConfirmClick()

        verify(homeViewModel).destinationReceived()
    }
}
