package com.yalta.services

import com.yalta.CoroutineTestRule
import com.yalta.repositories.FakeLogoutRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LogoutServiceUnitTest {
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private val repo = FakeLogoutRepo()

    @Test
    fun logoutTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        assertTrue(LogoutService(repo).logout())
    }

    @Test
    fun doubleLogoutTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val service = LogoutService(repo)
        assertTrue(service.logout())
        assertFalse(service.logout())
    }
}
