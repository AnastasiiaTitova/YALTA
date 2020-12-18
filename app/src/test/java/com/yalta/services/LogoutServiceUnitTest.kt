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
class LogoutServiceUnitTest {
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private val test = Mockito.mock(RealLogoutRepo::class.java)

    @Test
    fun logoutTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.logout()).thenReturn(LoggedOut())
        assertTrue(LogoutService(test).logout())
    }

    @Test
    fun doubleLogoutTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.logout()).thenReturn(FailedResponse(Reason.BAD_CODE))
        assertFalse(LogoutService(test).logout())
    }
}
