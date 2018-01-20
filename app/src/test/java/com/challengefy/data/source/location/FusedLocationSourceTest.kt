package com.challengefy.data.source.location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(value = [LocationServices::class, ContextCompat::class])
class FusedLocationSourceTest {

    @Test
    fun testUpdatesStopWhenDispose() {
        val request = LocationRequest()
        val location = mock(Location::class.java)

        robot {
            withPermission(PERMISSION_GRANTED)
            withLastLocation(false, null)
            withLocationUpdates(listOf(location))
            doTest {
                val test = positionUpdates(request).take(1).test()
                test.assertNoErrors()
                verifyUpdatesStopped()
            }
        }
    }

    @Test
    fun testLocationUpdateWithoutLastLocation() {
        val request = LocationRequest()
        val location = mock(Location::class.java)

        robot {
            withPermission(PERMISSION_GRANTED)
            withLastLocation(true, null)
            withLocationUpdates(listOf(location))
            doTest {
                val test = positionUpdates(request).test()
                test.assertValueCount(1)
                test.assertNoErrors()
            }
        }
    }

    @Test
    fun testLocationUpdateWithLastLocation() {
        val request = LocationRequest()
        val lastLocation = mock(Location::class.java)
        val location = mock(Location::class.java)

        robot {
            withPermission(PERMISSION_GRANTED)
            withLastLocation(true, lastLocation)
            withLocationUpdates(listOf(location))
            doTest {
                val test = positionUpdates(request).test()
                test.assertValueCount(2)
                test.assertNoErrors()
            }
        }
    }

    @Test
    fun testLocationUpdateWithLastLocationFailed() {
        val request = LocationRequest()
        val location = mock(Location::class.java)

        robot {
            withPermission(PERMISSION_GRANTED)
            withLastLocation(false, null)
            withLocationUpdates(listOf(location))
            doTest {
                val test = positionUpdates(request).test()
                test.assertValueCount(1)
            }
        }
    }

    private fun robot(func: Robot.() -> Unit) {
      func(Robot())
    }


    @Suppress("UNCHECKED_CAST")
    private class Robot {

        @Mock
        lateinit var context: Context

        @Mock
        lateinit var fusedProviderClient: FusedLocationProviderClient

        @Mock
        lateinit var task: Task<Location>

        val source: FusedLocationSource

        private var locationUpdatesCallback: LocationCallback? = null

        init {
            MockitoAnnotations.initMocks(this)
            PowerMockito.mockStatic(ContextCompat::class.java)
            PowerMockito.mockStatic(LocationServices::class.java)

            this.source = FusedLocationSource(context)
        }

        fun withPermission(permission: Int) = apply {
            `when`(ContextCompat.checkSelfPermission(any(), eq(ACCESS_FINE_LOCATION)))
                    .thenReturn(permission)
        }

        fun withLastLocation(success: Boolean, lastLocation: Location?) = apply {
            `when`(fusedProviderClient.lastLocation).thenReturn(task)
            `when`(task.isSuccessful).thenReturn(success)
            `when`(task.result).thenReturn(lastLocation)
            `when`(task.addOnCompleteListener(any())).then {
                val listener = it.arguments.first() as OnCompleteListener<Location>
                listener.onComplete(task)
                return@then task
            }
        }

        fun withLocationUpdates(locations: List<Location>) = apply {
            `when`(fusedProviderClient.requestLocationUpdates(any(), any(), any())).then {
                locationUpdatesCallback = it.arguments[1] as LocationCallback
                locationUpdatesCallback?.onLocationResult(LocationResult.create(locations))
                return@then task
            }
        }

        fun verifyUpdatesStopped() = apply {
            verify(fusedProviderClient).removeLocationUpdates(locationUpdatesCallback)
        }

        inline fun doTest(test: FusedLocationSource.() -> Unit) = apply {
            `when`(LocationServices.getFusedLocationProviderClient(any(Context::class.java)))
                    .thenReturn(fusedProviderClient)
            test(source)
        }

    }
}
