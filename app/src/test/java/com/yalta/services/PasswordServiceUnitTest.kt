package com.yalta.services

import com.yalta.CoroutineTestRule
import com.yalta.repositories.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class PasswordServiceUnitTest {
    private val test = mock(RealPasswordRepo::class.java)

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Test
    fun changePassword() = coroutinesTestRule.testDispatcher.runBlockingTest {
        `when`(test.changePassword("newPass")).thenReturn(PasswordChanged())
        assertTrue(PasswordService(test).changePassword("newPass").get())
    }

    @Test
    fun badChangePassword() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val service = PasswordService(test)
        `when`(test.changePassword("bad")).thenReturn(FailedResponse(Reason.BAD_CODE))
        assertFalse(service.changePassword("bad").get())
    }

    @Test
    fun connectionProblem() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val service = PasswordService(test)
        `when`(test.changePassword("bad2")).thenReturn(FailedResponse(Reason.FAILED_CONNECTION))
        assertFalse(service.changePassword("bad2").isPresent)
    }
}
