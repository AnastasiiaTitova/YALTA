package com.yalta.services

import com.yalta.CoroutineTestRule
import com.yalta.repositories.*
import common.Point
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class PointServiceUnitTest {
    private val test = mock(RealPointRepo::class.java)

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Test
    fun getEmptyPoints() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getAllPoints()).thenReturn(GotPoints(emptyList()))
        Assert.assertTrue(PointService(test).getAllPoints().isEmpty())
    }

    @Test
    fun getNotEmptyPoints() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val point = Point(1, 1.0, 1.0, "1")
        Mockito.`when`(test.getAllPoints()).thenReturn(GotPoints(listOf(point)))
        val result = PointService(test).getAllPoints()
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(1, result.size)
        Assert.assertEquals(point, result[0])
    }

    @Test
    fun failedGetPoints() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getAllPoints()).thenReturn(FailedResponse(Reason.BAD_CODE))
        Assert.assertTrue(PointService(test).getAllPoints().isEmpty())
    }
}
