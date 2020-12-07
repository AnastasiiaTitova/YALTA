package com.yalta.services

import com.yalta.CoroutineTestRule
import com.yalta.repositories.FakePasswordRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PasswordServiceUnitTest {
    private val repo = FakePasswordRepo()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Test
    fun changePassword() = coroutinesTestRule.testDispatcher.runBlockingTest {
        assertTrue(PasswordService(repo).changePassword("newPass"))
    }

    @Test
    fun badChangePassword() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val service = PasswordService(repo)

        assertTrue(service.changePassword("good"))
        assertFalse(service.changePassword("bad"))
    }
}
