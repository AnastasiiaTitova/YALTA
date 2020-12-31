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
class UserServiceUnitTest {
    private val test = Mockito.mock(RealAuthRepo::class.java)

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

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
