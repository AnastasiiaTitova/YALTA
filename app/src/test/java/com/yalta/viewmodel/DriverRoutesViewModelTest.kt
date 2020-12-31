package com.yalta.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yalta.CoroutineTestRule
import com.yalta.services.RoutesService
import common.Point
import common.Route
import common.RoutePoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.joda.time.DateTime
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class DriverRoutesViewModelTest {
    private lateinit var viewModel: DriverRoutesViewModel
    private val test = Mockito.mock(RoutesService::class.java)
    private val route = Route(1, 1, DateTime.now(), emptyList(), false)
    private val routeWithPoint =
        Route(1, 1, DateTime.now(), listOf(RoutePoint(1, Point(1, 1.0, 1.0, "first"), false, 1)), false)
    private val date = DateTime.now()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getRoutes(date.toString(), date.toString())).thenReturn(listOf(route))
        viewModel = DriverRoutesViewModel(test, coroutinesTestRule.testDispatcher)
    }

    @Test
    fun successfulGetRoutes() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getRoutes(date.minusDays(1).toString(), date.minusDays(1).toString()))
            .thenReturn(emptyList())
        viewModel.fromDate.value = date.minusDays(1)
        viewModel.toDate.value = date.minusDays(1)
        viewModel.getSomeRoutes()

        assertFalse(viewModel.showDatesError.value!!)
        assertTrue(viewModel.routes?.size == 0)
        assertTrue(viewModel.routesNames.value?.size == 0)
        assertNull(viewModel.selectedRoute)
        assertTrue(viewModel.selectedRoutePoints.value!!.isEmpty())
    }

    @Test
    fun successfulGetEmptyRoutes() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.fromDate.value = date
        viewModel.toDate.value = date
        viewModel.getSomeRoutes()

        assertFalse(viewModel.showDatesError.value!!)
        assertTrue(viewModel.routes?.size == 1)
        assertTrue(viewModel.routesNames.value?.size == 1)
        assertNull(viewModel.selectedRoute)
        assertTrue(viewModel.selectedRoutePoints.value!!.isEmpty())
    }

    @Test
    fun failedGetRoutesBadDates() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getRoutes(date.toString(), date.minusDays(1).toString())).thenReturn(listOf(route))
        viewModel.fromDate.value = date
        viewModel.toDate.value = date.minusDays(1)
        viewModel.getSomeRoutes()

        assertTrue(viewModel.showDatesError.value!!)
    }

    @Test
    fun failedGetRoutesBadResponse() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getRoutes(date.toString(), date.plusDays(1).toString())).thenReturn(null)
        viewModel.fromDate.value = date
        viewModel.toDate.value = date.plusDays(1)
        viewModel.getSomeRoutes()

        assertFalse(viewModel.showDatesError.value!!)
    }

    @Test
    fun changeSelectedValue() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.fromDate.value = date
        viewModel.toDate.value = date
        viewModel.getSomeRoutes()
        viewModel.selectedRouteChanged(0)

        assertNotNull(viewModel.selectedRoutePoints.value)
        assertTrue(viewModel.selectedRoutePoints.value!!.isEmpty())
    }

    @Test
    fun changeSelectedBadValue() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.fromDate.value = date
        viewModel.toDate.value = date
        viewModel.getSomeRoutes()
        viewModel.selectedRouteChanged(1)

        assertNotNull(viewModel.selectedRoutePoints.value)
        assertTrue(viewModel.selectedRoutePoints.value!!.isEmpty())
    }

    @Test
    fun changeSelectedValueWithPoints() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getRoutes(date.minusDays(2).toString(), date.toString())).thenReturn(listOf(routeWithPoint))

        viewModel.fromDate.value = date.minusDays(2)
        viewModel.toDate.value = date
        viewModel.getSomeRoutes()
        viewModel.selectedRouteChanged(0)

        assertNotNull(viewModel.selectedRoutePoints.value)
        assertTrue(viewModel.selectedRoutePoints.value?.size == 1)
    }

}
