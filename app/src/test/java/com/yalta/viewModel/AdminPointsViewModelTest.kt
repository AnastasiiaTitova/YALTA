package com.yalta.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yalta.CoroutineTestRule
import com.yalta.services.PointService
import common.Point
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class AdminPointsViewModelTest {
    private lateinit var viewModel: AdminPointsViewModel
    private val test = Mockito.mock(PointService::class.java)
    private val point = Point(1, 1.0, 1.0, "1")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getAllPoints()).thenReturn(listOf(point))
        viewModel = AdminPointsViewModel(test, coroutinesTestRule.testDispatcher)
    }

    @Test
    fun setupTest() {
        assertEquals(1, viewModel.points.value?.size!!)
        assertEquals(point, viewModel.points.value!![0].data)
    }

    @Test
    fun nullFilterTest() {
        viewModel.search.value = null
        viewModel.filterPoints()
        assertEquals(1, viewModel.points.value?.size!!)
        assertEquals(point, viewModel.points.value!![0].data)
    }

    @Test
    fun emptyFilterTest() {
        viewModel.search.value = ""
        viewModel.filterPoints()
        assertEquals(1, viewModel.points.value?.size!!)
        assertEquals(point, viewModel.points.value!![0].data)
    }

    @Test
    fun goodFilterMatchingTest() {
        viewModel.search.value = "1"
        viewModel.filterPoints()
        assertEquals(1, viewModel.points.value?.size!!)
        assertEquals(point, viewModel.points.value!![0].data)
    }

    @Test
    fun goodFilterNotMatchingTest() {
        viewModel.search.value = "2"
        viewModel.filterPoints()
        assertEquals(0, viewModel.points.value?.size!!)
    }

    @Test
    fun updatePoints() {
        viewModel.updatePoints()
        setupTest()
    }
}
