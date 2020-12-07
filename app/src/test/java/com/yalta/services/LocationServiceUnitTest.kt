package com.yalta.services

import android.location.Location
import com.yalta.CoroutineTestRule
import com.yalta.repositories.FakeLocationRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LocationServiceUnitTest {
    private val repo = FakeLocationRepo()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Test
    fun addLocationTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val location = Location("")
        location.latitude = 10.0
        location.longitude = 15.0

        val response = LocationService(repo).sendCurrentLocation(location)

        assertNotNull(response)
        assertEquals(location.latitude, response?.lat)
        assertEquals(location.longitude, response?.lon)
        assertEquals(1L, response?.userId)
    }
}
