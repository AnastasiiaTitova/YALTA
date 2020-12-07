package com.yalta.services

import com.yalta.CoroutineTestRule
import com.yalta.repositories.FailedResponse
import com.yalta.repositories.FakePasswordRepo
import com.yalta.repositories.PasswordChanged
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class PasswordServiceUnitTest {
    private val test = mock(FakePasswordRepo::class.java)

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Test
    fun changePassword() = coroutinesTestRule.testDispatcher.runBlockingTest {
        `when`(test.changePassword("newPass")).thenReturn(PasswordChanged())
        assertTrue(PasswordService(test).changePassword("newPass"))
    }

    @Test
    fun badChangePassword() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val service = PasswordService(test)
        `when`(test.changePassword("good")).thenReturn(PasswordChanged())
        `when`(test.changePassword("bad")).thenReturn(FailedResponse())

        assertTrue(service.changePassword("good"))
        assertFalse(service.changePassword("bad"))
    }
}
