package com.challengefy.feature.ride.viewmodel

import android.databinding.ObservableField
import com.challengefy.data.model.Address
import com.challengefy.data.repository.LocationRepository
import com.challengefy.data.repository.LocationRepository.LocationState.*
import com.challengefy.data.repository.PlaceRepository
import com.challengefy.feature.ride.navigator.HomeNavigator
import com.challengefy.test.KotlinArgumentMatchers.any
import com.challengefy.feature.ride.viewmodel.PickupViewModel.ViewState.LOCATION_DISABLED
import com.challengefy.test.Fabricator
import com.challengefy.test.TestSchedulerManager
import io.reactivex.Maybe
import io.reactivex.Single
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class PickupViewModelTest {

    @Mock
    lateinit var navigator: HomeNavigator

    @Mock
    lateinit var homeViewModel: HomeViewModel

    @Mock
    lateinit var placeRepository: PlaceRepository

    @Mock
    lateinit var locationRepository: LocationRepository

    val pickUpAddress = ObservableField<Address>()

    val schedulerManager = TestSchedulerManager()

    lateinit var viewModel: PickupViewModel

    @Before
    fun setUp() {
        `when`(homeViewModel.pickUpAddress).thenReturn(pickUpAddress)

        viewModel = PickupViewModel(navigator, homeViewModel, locationRepository, placeRepository, schedulerManager)
    }

    @Test
    fun testViewModelWhenLocationDisabled() {
        `when`(locationRepository.getLocationState()).thenReturn(Single.just(DISABLED))

        viewModel.init()

        assertThat(viewModel.viewState.get(), equalTo(LOCATION_DISABLED))
    }

    @Test
    fun testViewModelWhenLocationNoPermission() {
        `when`(locationRepository.getLocationState()).thenReturn(Single.just(NO_PERMISSION))

        `when`(navigator.shouldShowRationaleLocation()).thenReturn(false)

        viewModel.init()

        verify(navigator).requestLocationPermission()
    }

    @Test
    fun testViewModelWhenLocationNoPermissionWithRationale() {
        `when`(locationRepository.getLocationState()).thenReturn(Single.just(NO_PERMISSION))

        `when`(navigator.shouldShowRationaleLocation()).thenReturn(true)

        viewModel.init()

        assertThat(viewModel.viewState.get(), equalTo(PickupViewModel.ViewState.LOCATION_NO_PERMISSION))
    }

    @Test
    fun testLocationActive() {
        val address = Fabricator.address()
        `when`(placeRepository.getCurrentPlace()).thenReturn(Single.just(address))
        `when`(locationRepository.getUserLocation()).thenReturn(Single.just(address.position))
        `when`(locationRepository.getLocationState()).thenReturn(Single.just(ACTIVE))

        viewModel.init()

        schedulerManager.timeScheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)

        assertThat(viewModel.pickUpAddress.get(), equalTo(address))
    }

    @Test
    fun testLocationActiveButCurrentPlaceIsTooFar() {
        val farAddress = Fabricator.address(position = Fabricator.position(23.0, 46.0))
        val closeAddress = Fabricator.address()

        `when`(placeRepository.getCurrentPlace()).thenReturn(Single.just(farAddress))
        `when`(locationRepository.getUserLocation()).thenReturn(Single.just(Fabricator.position()))

        `when`(placeRepository.getAddressByPosition(any())).thenReturn(Maybe.just(closeAddress))
        `when`(locationRepository.getLocationState()).thenReturn(Single.just(ACTIVE))

        viewModel.init()

        schedulerManager.timeScheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)

        assertThat(viewModel.pickUpAddress.get(), equalTo(closeAddress))
    }

    @Test
    fun testViewModelLocationActivityResultShouldRequestLocationAgain() {
        `when`(placeRepository.getCurrentPlace()).thenReturn(Single.just(Fabricator.address()))
        `when`(locationRepository.getLocationState()).thenReturn(Single.just(ACTIVE))

        viewModel.onActivityResult(HomeNavigator.REQUEST_CODE_LOCATION_SETTINGS, 0)
        viewModel.onActivityResult(HomeNavigator.REQUEST_CODE_RESOLUTION, 0)
        viewModel.onActivityResult(HomeNavigator.REQUEST_CODE_APP_SETTINGS, 0)

        verify(locationRepository, times(3)).getLocationState()
    }
}
