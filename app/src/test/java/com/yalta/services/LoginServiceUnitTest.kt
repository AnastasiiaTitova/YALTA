package com.yalta.services

import com.yalta.CoroutineTestRule
import com.yalta.repositories.FakeLoginRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginServiceUnitTest {
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private val repo = FakeLoginRepo()

    @Test
    fun correct_credentials_test() = coroutinesTestRule.testDispatcher.runBlockingTest {
        assertTrue(LoginService(repo).login("root", "root"))
    }

    @Test
    fun wrong_credentials_test() = coroutinesTestRule.testDispatcher.runBlockingTest {
        assertFalse(LoginService(repo).login("user", "password"))
    }
}
