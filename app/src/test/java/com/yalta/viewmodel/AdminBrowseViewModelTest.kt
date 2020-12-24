package com.yalta.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yalta.CoroutineTestRule
import com.yalta.repositories.GotPoints
import com.yalta.repositories.PointRepo
import common.Point
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class AdminBrowseViewModelTest {
    private lateinit var viewModel: AdminBrowseViewModel
    private val test = Mockito.mock(PointRepo::class.java)
    private val point = Point(1, 1.0, 1.0, "1")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getAllPoints()).thenReturn(GotPoints(listOf(point)))
        viewModel = AdminBrowseViewModel(test, coroutinesTestRule.testDispatcher)
    }

    @Test
    fun setupTest() {
        Assert.assertEquals(1, viewModel.points.value?.size!!)
        Assert.assertEquals(point, viewModel.points.value!![0].data)
    }
}
