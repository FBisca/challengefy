package com.challengefy.data.repository

import com.challengefy.data.repository.LocationRepository.LocationState.*
import com.challengefy.data.source.location.LocationSource
import com.challengefy.test.KotlinArgumentMatchers.any
import io.reactivex.Single
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocationRepositoryTest {

    @Mock
    lateinit var locationSource: LocationSource

    lateinit var repository: LocationRepository

    @Before
    fun setUp() {
        repository = LocationRepositoryImpl(locationSource)
    }

    @Test
    fun testLocationStateWhenLocationIsDisabled() {
        `when`(locationSource.isLocationEnabled(any())).thenReturn(Single.just(false))
        `when`(locationSource.isPermissionGranted()).thenReturn(Single.just(true))

        val test = repository.getLocationState().test()

        test.assertNoErrors()
        assertThat(test.values().first(), equalTo(DISABLED))
    }

    @Test
    fun testLocationStateWhenLocationHasNoPermission() {
        `when`(locationSource.isLocationEnabled(any())).thenReturn(Single.just(true))
        `when`(locationSource.isPermissionGranted()).thenReturn(Single.just(false))

        val test = repository.getLocationState().test()

        test.assertNoErrors()
        assertThat(test.values().first(), equalTo(NO_PERMISSION))
    }

    @Test
    fun testLocationStateWhenActive() {
        `when`(locationSource.isLocationEnabled(any())).thenReturn(Single.just(true))
        `when`(locationSource.isPermissionGranted()).thenReturn(Single.just(true))

        val test = repository.getLocationState().test()

        test.assertNoErrors()
        assertThat(test.values().first(), equalTo(ACTIVE))
    }
}
