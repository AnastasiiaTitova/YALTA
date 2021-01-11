package com.yalta.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yalta.CoroutineTestRule
import com.yalta.services.RoutesService
import com.yalta.services.StorageImpl
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
import java.util.*

@ExperimentalCoroutinesApi
class DriverRoutesViewModelTest {
    private lateinit var viewModel: DriverRoutesViewModel
    private val test = Mockito.mock(RoutesService::class.java)
    private val storage = StorageImpl()
    private val route = Route(1, 1, DateTime.now(), emptyList(), false)
    private val routeWithPoint =
        Route(1, 1, DateTime.now(), listOf(RoutePoint(1, Point(1, 1.0, 1.0, "first"), false, 1, DateTime.now())), false)
    private val date = DateTime.now()
    private val fromDate = date.withHourOfDay(0).withMinuteOfHour(0)
    private val toDate = date.withHourOfDay(23).withMinuteOfHour(59)


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getRoutes(fromDate.toString(), toDate.toString())).thenReturn(Optional.of(listOf(route)))
        viewModel = DriverRoutesViewModel(test, storage, coroutinesTestRule.testDispatcher)
    }

    @Test
    fun successfulGetRoutes() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getRoutes(fromDate.minusDays(1).toString(), toDate.minusDays(1).toString()))
            .thenReturn(Optional.of(emptyList()))
        viewModel.fromDate.value = fromDate.minusDays(1)
        viewModel.toDate.value = toDate.minusDays(1)
        viewModel.getSomeRoutes()

        assertTrue(viewModel.storage.routes.value?.size == 0)
        assertNull(viewModel.selectedRoute)
        assertTrue(viewModel.selectedRoutePoints.value!!.isEmpty())
    }

    @Test
    fun successfulGetEmptyRoutes() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.fromDate.value = fromDate
        viewModel.toDate.value = toDate
        viewModel.getSomeRoutes()

        assertFalse(viewModel.isRouteCurrent.value!!)
        assertTrue(viewModel.storage.routes.value?.size == 1)
        assertNull(viewModel.selectedRoute)
        assertTrue(viewModel.selectedRoutePoints.value!!.isEmpty())
    }

    @Test
    fun failedGetRoutesBadResponse() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getRoutes(fromDate.toString(), toDate.plusDays(1).toString())).thenReturn(Optional.empty())
        viewModel.fromDate.value = fromDate
        viewModel.toDate.value = toDate.plusDays(1)
        viewModel.getSomeRoutes()
    }

    @Test
    fun changeSelectedValue() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.fromDate.value = fromDate
        viewModel.toDate.value = toDate
        viewModel.getSomeRoutes()
        viewModel.selectedRouteChanged(0)

        assertNotNull(viewModel.selectedRoutePoints.value)
        assertTrue(viewModel.selectedRoutePoints.value!!.isEmpty())
    }

    @Test
    fun changeSelectedBadValue() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.fromDate.value = fromDate
        viewModel.toDate.value = toDate
        viewModel.getSomeRoutes()
        viewModel.selectedRouteChanged(1)

        assertNotNull(viewModel.selectedRoutePoints.value)
        assertTrue(viewModel.selectedRoutePoints.value!!.isEmpty())
    }

    @Test
    fun changeSelectedValueWithPoints() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getRoutes(fromDate.minusDays(2).toString(), toDate.toString()))
            .thenReturn(Optional.of(listOf(routeWithPoint)))

        viewModel.fromDate.value = fromDate.minusDays(2)
        viewModel.toDate.value = toDate
        viewModel.getSomeRoutes()
        viewModel.selectedRouteChanged(0)

        assertNotNull(viewModel.selectedRoutePoints.value)
        assertTrue(viewModel.selectedRoutePoints.value?.size == 1)
    }

}
