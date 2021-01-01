package com.yalta.services

import com.yalta.CoroutineTestRule
import com.yalta.repositories.*
import common.Route
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.joda.time.DateTime
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class RoutesServiceUnitTest {
    private val test = Mockito.mock(RealRoutesRepo::class.java)

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Test
    fun goodRoutesTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getRoutes("today", "today")).thenReturn(GotRoutes(emptyList()))

        val res = RoutesService(test).getRoutes("today", "today")
        assertTrue(res.isPresent)
        assertTrue(res.get().isEmpty())
    }

    @Test
    fun badRoutesTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getRoutes("tomorrow", "today")).thenReturn(FailedResponse(Reason.BAD_CODE))

        val res = RoutesService(test).getRoutes("tomorrow", "today")
        assertFalse(res.isPresent)
    }

    @Test
    fun getCurrentRouteTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val route = Route(1,1, DateTime.now(), emptyList(), false)
        Mockito.`when`(test.getCurrentRoute())
            .thenReturn(GotRoute(route))
        val response = RoutesService(test).getCurrentRoute()

        assertNotNull(response)
        assertEquals(route.id, response?.id)
        assertEquals(route.driverId, response?.driverId)
        assertEquals(route.routeDate, response?.routeDate)
        assertEquals(route.finished, response?.finished)
    }

    @Test
    fun failedGetCurrentRouteTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getCurrentRoute())
            .thenReturn(FailedResponse(Reason.BAD_CODE))
        val response = RoutesService(test).getCurrentRoute()

        assertNull(response)
    }
}
