package com.yalta.services

import android.location.Location
import com.yalta.CoroutineTestRule
import com.yalta.repositories.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.joda.time.DateTime
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class LocationServiceUnitTest {
    private val test = Mockito.mock(RealLocationRepo::class.java)

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Test
    fun addLocationTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val location = Location("")
        location.latitude = 10.0
        location.longitude = 15.0

        Mockito.`when`(test.sendCurrentLocation(location))
            .thenReturn(AddedLocation(common.Location(1, location.latitude, location.longitude, 1, DateTime.now())))
        val response = LocationService(test).sendCurrentLocation(location)

        assertNotNull(response)
        assertEquals(location.latitude, response?.lat)
        assertEquals(location.longitude, response?.lon)
        assertEquals(1L, response?.userId)
    }

    @Test
    fun failedAddLocationTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val location = Location("")

        Mockito.`when`(test.sendCurrentLocation(location))
            .thenReturn(FailedResponse(Reason.BAD_CODE))
        val response = LocationService(test).sendCurrentLocation(location)

        assertNull(response)
    }

    @Test
    fun failedConnectionTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val location = Location("")

        Mockito.`when`(test.sendCurrentLocation(location))
            .thenReturn(FailedResponse(Reason.FAILED_CONNECTION))
        val response = LocationService(test).sendCurrentLocation(location)

        assertNull(response)
    }
}
