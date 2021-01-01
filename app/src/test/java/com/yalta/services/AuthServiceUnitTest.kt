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
class AuthServiceUnitTest {
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private val test = Mockito.mock(RealAuthRepo::class.java)

    @Test
    fun correctCredentialsTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.login("root", "root")).thenReturn(SuccessfulLogin("token", Driver))
        assertTrue(AuthService(test).login("root", "root").get())
    }

    @Test
    fun wrongCredentialsTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.login("user", "password")).thenReturn(FailedResponse(Reason.BAD_CODE))
        assertFalse(AuthService(test).login("user", "password").get())
    }

    @Test
    fun connectionProblemTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.login("connection", "problem")).thenReturn(FailedResponse(Reason.FAILED_CONNECTION))
        assertFalse(AuthService(test).login("connection", "problem").isPresent)
    }

    @Test
    fun logoutTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.logout()).thenReturn(LoggedOut())
        assertTrue(AuthService(test).logout())
    }

    @Test
    fun doubleLogoutTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.logout()).thenReturn(FailedResponse(Reason.BAD_CODE))
        assertFalse(AuthService(test).logout())
    }

    @Test
    fun getUserTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getUser()).thenReturn(GotUser(common.User(1, "user", "", Driver)))
        val response = AuthService(test).getUser()
        assertNotNull(response)
        assertEquals(1L, response?.id)
        assertEquals("user", response?.name)
    }

    @Test
    fun failedGetUserTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.getUser()).thenReturn(FailedResponse(Reason.BAD_CODE))
        val response = AuthService(test).getUser()
        assertNull(response)
    }
}
