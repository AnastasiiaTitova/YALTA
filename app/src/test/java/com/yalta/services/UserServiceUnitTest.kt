package com.yalta.services

import com.yalta.CoroutineTestRule
import com.yalta.repositories.FakeUserRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserServiceUnitTest {
    private val repo = FakeUserRepo()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Test
    fun get_correct_user_test() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val response = UserService(repo).getUser()
        assertNotNull(response)
        assertEquals(1L, response?.id)
    }

    @Test
    fun get_incorrect_user_test() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val response = UserService(repo).getUser(2)
        assertNull(response)
    }
}
