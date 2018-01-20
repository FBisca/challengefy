package com.challengefy.data.api

import com.challengefy.BuildConfig
import com.challengefy.base.di.module.NetworkModule
import com.challengefy.data.network.api.EstimateApi
import com.challengefy.data.network.interceptors.AuthInterceptor
import com.challengefy.data.network.request.EstimateRideRequest
import com.challengefy.data.network.request.Stop
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection.HTTP_OK
import java.util.*

class EstimateApiTest {

    val mockWebserver = MockWebServer()
    val locale = Locale.US

    lateinit var estimateApi: EstimateApi

    @Before
    fun setUp() {
        mockWebserver.start()
        val networkModule = NetworkModule()
        estimateApi = networkModule.provideRetrofit(
                networkModule.provideOkHttpClient(AuthInterceptor(locale), HttpLoggingInterceptor()),
                mockWebserver.url("/")
        ).create(EstimateApi::class.java)
    }

    @After
    fun dispose() {
        mockWebserver.shutdown()
    }

    @Test
    fun testHeaders() {
        mockWebserver.enqueue(MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(response()))

        val body = createEstimateRequestBody()

        val test = estimateApi.estimateRide(body).test()

        test.assertNoErrors()

        val headers = mockWebserver.takeRequest().headers
        assertThat(headers["Authorization"], equalTo("Bearer ${BuildConfig.AUTH_TOKEN}"))
        assertThat(headers["Accept-Language"], equalTo(locale.toLanguageTag()))
    }

    @Test
    fun testResponseParse() {
        mockWebserver.enqueue(MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(response()))

        val body = createEstimateRequestBody()

        val test = estimateApi.estimateRide(body).test()

        test.assertNoErrors()

        val response = test.values().first()
        val first = response.first()

        assertThat(response.size, equalTo(1))
        assertThat(first.price, equalTo("12.34€"))
        assertThat(first.vehicle.name, equalTo("Executive Class"))
        assertThat(first.vehicle.id, equalTo("executive_id"))
        assertThat(first.eta.time, equalTo(">2 min"))
    }

    private fun createEstimateRequestBody(): EstimateRideRequest {
        return EstimateRideRequest(stops = listOf(
                Stop(
                        position = listOf(23.0, 46.0),
                        name = "Name",
                        address = "Address"
                )
        ))
    }

    fun response() = """
        [
          {
            "vehicle_type": {
              "_id": "executive_id",
              "name": "Executive Class",
              "short_name": "Executive",
              "description": "A very large vehicle with comfortable seats",
              "icons": {
                "regular": "https://cabify.com/images/icons/vehicle_type/executive_27.png"
              },
              "icon": "executive",
              "service_type": "standard"
            },
            "total_price": 1234,
            "price_formatted": "12.34€",
            "currency": "EUR",
            "currency_symbol": "€",
            "eta": {
              "min": 100,
              "max": 1000,
              "formatted": ">2 min"
            }
          }
    ]
    """
}
