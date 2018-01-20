@file:Suppress("UNCHECKED_CAST")

package com.challengefy.data.source.place

import android.content.Context
import com.challengefy.data.model.PredictionAddress
import com.google.android.gms.location.places.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*

@RunWith(PowerMockRunner::class)
@PrepareForTest(value = [Places::class])
class GooglePlaceSourceTest {

    @Test
    fun testPredictionsParse() {
        predictionTest {
            val query = "Test"
            val northeast = LatLng(0.0, 0.0)
            val southwest = LatLng(0.0, 0.0)

            val prediction = mock(AutocompletePrediction::class.java)
            `when`(prediction.placeId).thenReturn("1")
            `when`(prediction.getPrimaryText(any())).thenReturn("Google Inc")
            `when`(prediction.getSecondaryText(any())).thenReturn("Street X - San Francisco, CA")

            withQuery("Test")
            withResult(true, listOf(prediction))

            test {
                val subscriber = autoCompletePlaces(query, northeast, southwest).test()

                subscriber.assertNoErrors()
                subscriber.assertValueCount(1)

                val list = subscriber.values().first()
                val predictionAddress = list.first()

                assertThat(predictionAddress.placeId, equalTo("1"))
                assertThat(predictionAddress.title, equalTo("Google Inc"))
                assertThat(predictionAddress.description, equalTo("Street X - San Francisco, CA"))
            }
        }
    }

    @Test
    fun testPlaceDetail() {
        placeDetailTest {
            val prediction = PredictionAddress("1", "Google Inc", "Street X - San Francisco, CA")
            val position = LatLng(10.0, 30.0)

            val place = mock(Place::class.java)
            `when`(place.latLng).thenReturn(position)
            `when`(place.isDataValid).thenReturn(true)

            withPrediction(prediction)
            withResult(true, listOf(place))

            test {
                val subscriber = detailPrediction(prediction).test()

                subscriber.assertNoErrors()
                subscriber.assertValueCount(1)

                val result = subscriber.values().first()

                assertThat(result.title, equalTo("Google Inc"))
                assertThat(result.description, equalTo("Street X - San Francisco, CA"))
                assertThat(result.position.latitude, equalTo(position.latitude))
                assertThat(result.position.longitude, equalTo(position.longitude))
            }
        }
    }

    private inline fun predictionTest(block: PredictionRobot.() -> Unit) = block(PredictionRobot())

    private inline fun placeDetailTest(block: DetailRobot.() -> Unit) = block(DetailRobot())

    class PredictionRobot {

        @Mock
        lateinit var context: Context

        @Mock
        lateinit var geoDataClient: GeoDataClient

        @Mock
        lateinit var task: Task<AutocompletePredictionBufferResponse>

        @Mock
        lateinit var result: AutocompletePredictionBufferResponse

        init {
            MockitoAnnotations.initMocks(this)

            PowerMockito.mockStatic(Places::class.java)
            `when`(Places.getGeoDataClient(any<Context>(), any())).thenReturn(geoDataClient)
        }

        fun withQuery(query: String) = apply {
            `when`(geoDataClient.getAutocompletePredictions(eq(query), any(), any())).thenReturn(task)
        }

        fun withResult(success: Boolean, list: List<AutocompletePrediction>) = apply {
            `when`(result.iterator()).thenReturn(list.toMutableList().listIterator())
            `when`(task.isSuccessful).thenReturn(success)
            `when`(task.result).thenReturn(result)
            `when`(task.addOnCompleteListener(any())).then {
                val callback = it.arguments[0] as OnCompleteListener<AutocompletePredictionBufferResponse>
                callback.onComplete(task)
                task
            }
        }

        inline fun test(block: GooglePlaceSource.() -> Unit) {
            block(GooglePlaceSource(context, Locale.UK))
        }

    }

    class DetailRobot {

        @Mock
        lateinit var context: Context

        @Mock
        lateinit var geoDataClient: GeoDataClient

        @Mock
        lateinit var task: Task<PlaceBufferResponse>

        @Mock
        lateinit var result: PlaceBufferResponse

        init {
            MockitoAnnotations.initMocks(this)

            PowerMockito.mockStatic(Places::class.java)
            `when`(Places.getGeoDataClient(any<Context>(), any())).thenReturn(geoDataClient)
        }

        fun withPrediction(prediction: PredictionAddress) = apply {
            `when`(geoDataClient.getPlaceById(eq(prediction.placeId))).thenReturn(task)
        }

        fun withResult(success: Boolean, list: List<Place>) = apply {
            `when`(result.iterator()).thenReturn(list.toMutableList().listIterator())
            `when`(task.isSuccessful).thenReturn(success)
            `when`(task.result).thenReturn(result)
            `when`(task.addOnCompleteListener(any())).then {
                val callback = it.arguments[0] as OnCompleteListener<PlaceBufferResponse>
                callback.onComplete(task)
                task
            }
        }

        inline fun test(block: GooglePlaceSource.() -> Unit) {
            block(GooglePlaceSource(context, Locale.UK))
        }

    }
}
