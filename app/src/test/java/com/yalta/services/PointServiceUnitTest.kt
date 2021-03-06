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

    @Test
    fun failedNewPoint() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val name = "name"
        val coordinate = 0.0
        Mockito.`when`(test.createNewPoint(name, coordinate, coordinate)).thenReturn(FailedResponse(Reason.BAD_CODE))
        Assert.assertFalse(PointService(test).createNewPoint(name, coordinate, coordinate))
    }

    @Test
    fun successfulNewPoint() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val name = "name1"
        val coordinate = 0.0
        Mockito.`when`(test.createNewPoint(name, coordinate, coordinate)).thenReturn(PointCreated())
        Assert.assertTrue(PointService(test).createNewPoint(name, coordinate, coordinate))
    }

    @Test
    fun failedUpdatePointName() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val id = 1L
        val name = "name"
        val coordinate = 0.0
        Mockito.`when`(test.updatePointName(id, name, coordinate, coordinate)).thenReturn(FailedResponse(Reason.BAD_CODE))
        Assert.assertFalse(PointService(test).updatePointName(id, name, coordinate, coordinate))
    }

    @Test
    fun successfulUpdatePointName() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val id = 1L
        val name = "name1"
        val coordinate = 0.0
        Mockito.`when`(test.updatePointName(id, name, coordinate, coordinate)).thenReturn(PointUpdated())
        Assert.assertTrue(PointService(test).updatePointName(id, name, coordinate, coordinate))
    }

    @Test
    fun failedUpdatePointPosition() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val id = 1L
        val name = "name"
        val coordinate = 0.0
        Mockito.`when`(test.updatePointPosition(id, name, coordinate, coordinate)).thenReturn(FailedResponse(Reason.BAD_CODE))
        Assert.assertFalse(PointService(test).updatePointPosition(id, name, coordinate, coordinate))
    }

    @Test
    fun successfulUpdatePointPosition() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val id = 1L
        val name = "name1"
        val coordinate = 0.0
        Mockito.`when`(test.updatePointPosition(id, name, coordinate, coordinate)).thenReturn(PointUpdated())
        Assert.assertTrue(PointService(test).updatePointPosition(id, name, coordinate, coordinate))
    }
}
