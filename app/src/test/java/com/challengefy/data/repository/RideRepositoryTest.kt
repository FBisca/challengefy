package com.challengefy.data.repository

import com.challengefy.data.model.Address
import com.challengefy.data.model.Estimate
import com.challengefy.data.network.api.EstimateApi
import com.challengefy.data.network.request.EstimateRideRequest
import com.challengefy.data.network.request.Stop
import com.challengefy.test.Fabricator
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
class RideRepositoryTest {

    @Mock
    lateinit var estimateApi: EstimateApi

    lateinit var repository: RideRepository

    @Before
    fun setUp() {
        repository = RideRepositoryImpl(estimateApi)
    }

    @Test
    fun verifyRequestIsBeingPopulatedCorrectly() {
        val start = Fabricator.address("Title 1", position = Fabricator.position(23.0, 46.0))
        val end = Fabricator.address("Title 2", position = Fabricator.position(24.0, 48.0))

        `when`(estimateApi.estimateRide(any())).then {
            val request = it.arguments[0] as EstimateRideRequest

            val first = request.stops[0]
            val last = request.stops[1]


            assertEquals(first, start)
            assertEquals(last, end)

            Single.just(emptyList<Estimate>())
        }

        repository.estimateRide(start, end)
    }

    private fun assertEquals(last: Stop, end: Address) {
        assertThat(last.name, equalTo(end.title))
        assertThat(last.address, equalTo(end.description))
        assertThat(last.position[0], equalTo(end.position.latitude))
        assertThat(last.position[1], equalTo(end.position.longitude))
    }
}
