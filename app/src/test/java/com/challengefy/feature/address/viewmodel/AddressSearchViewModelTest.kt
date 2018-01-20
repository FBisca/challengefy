package com.challengefy.feature.address.viewmodel

import com.challengefy.data.repository.PlaceRepository
import com.challengefy.test.TestSchedulerManager
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class AddressSearchViewModelTest {

    @Mock
    lateinit var placesRepository: PlaceRepository

    lateinit var viewModel: AddressSearchViewModel

    private val schedulerManager = TestSchedulerManager()

    @Before
    fun setUp() {
        viewModel = AddressSearchViewModel(placesRepository, schedulerManager)
    }

    @Test
    fun testInputIsCallingPlaces() {
        `when`(placesRepository.autoComplete("T", null, null)).thenReturn(Single.just(emptyList()))

        val test = viewModel.inputObservable().test()

        viewModel.inputSearch("T")
        schedulerManager.timeScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)

        schedulerManager.timeScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)

        test.assertNoErrors()
        verify(placesRepository).autoComplete("T", null, null)
    }

    @Test
    fun testInputDebounce() {
        val test = viewModel.inputObservable().test()

        viewModel.inputSearch("T")
        schedulerManager.timeScheduler.advanceTimeBy(200, TimeUnit.MILLISECONDS)
        viewModel.inputSearch("o")
        schedulerManager.timeScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)

        test.assertNoErrors()
        verify(placesRepository, never()).autoComplete("To", null, null)
    }
}
