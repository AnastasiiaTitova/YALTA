package com.yalta.services

import com.yalta.CoroutineTestRule
import com.yalta.repositories.*
import common.Driver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class LoginServiceUnitTest {
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private val test = Mockito.mock(RealLoginRepo::class.java)

    @Test
    fun correctCredentialsTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.login("root", "root")).thenReturn(SuccessfulLogin("token", Driver))
        assertTrue(LoginService(test).login("root", "root").get())
    }

    @Test
    fun wrongCredentialsTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.login("user", "password")).thenReturn(FailedResponse(Reason.BAD_CODE))
        assertFalse(LoginService(test).login("user", "password").get())
    }

    @Test
    fun connectionProblemTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.login("connection", "problem")).thenReturn(FailedResponse(Reason.FAILED_CONNECTION))
        assertFalse(LoginService(test).login("connection", "problem").isPresent)
    }
}
