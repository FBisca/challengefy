package com.challengefy.feature.ride.viewmodel

import com.challengefy.feature.ride.navigator.HomeNavigator
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ConfirmPickupViewModelTest {

    @Mock
    lateinit var navigator: HomeNavigator

    @Mock
    lateinit var homeViewModel: HomeViewModel

    lateinit var viewModel: ConfirmPickupViewModel

    @Before
    fun setUp() {
        viewModel = ConfirmPickupViewModel(
                navigator,
                homeViewModel
        )
    }

    @Test
    fun testConfirmPickUp() {
        viewModel.onConfirmClick()

        verify(homeViewModel).pickUpConfirmed()
    }
}
