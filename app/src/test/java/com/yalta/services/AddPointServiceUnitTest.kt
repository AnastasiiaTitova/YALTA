package com.yalta.services

import com.yalta.CoroutineTestRule
import com.yalta.repositories.FailedResponse
import com.yalta.repositories.PointCreated
import com.yalta.repositories.RealPointRepo
import com.yalta.repositories.Reason
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class AddPointServiceUnitTest {
    private val test = Mockito.mock(RealPointRepo::class.java)

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Test
    fun failedNewPoint() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val name = "name"
        val coordinate = 0.0
        Mockito.`when`(test.createNewPoint(name, coordinate, coordinate)).thenReturn(FailedResponse(Reason.BAD_CODE))
        assertFalse(AddPointService(test).createNewPoint(name, coordinate, coordinate))
    }

    @Test
    fun successfulNewPoint() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val name = "name1"
        val coordinate = 0.0
        Mockito.`when`(test.createNewPoint(name, coordinate, coordinate)).thenReturn(PointCreated())
        assertTrue(AddPointService(test).createNewPoint(name, coordinate, coordinate))
    }

}
