package com.yalta.services

import com.yalta.CoroutineTestRule
import com.yalta.repositories.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
        assertTrue(res!!.isEmpty())
    }

    @Test
    fun badRoutesTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getRoutes("tomorrow", "today")).thenReturn(FailedResponse(Reason.BAD_CODE))

        val res = RoutesService(test).getRoutes("tomorrow", "today")
        assertTrue(res == null)
    }
}
