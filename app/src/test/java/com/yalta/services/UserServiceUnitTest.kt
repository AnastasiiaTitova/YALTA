package com.yalta.services

import org.junit.Assert.*
import org.junit.Test

class UserServiceUnitTest {
    private val repo = HardcodedUserRepo()

    @Test
    fun get_correct_user_test() {
        val response = UserService(repo).getUser()
        assertNotNull(response)
        assertEquals(1L, response?.id)
    }

    @Test
    fun get_incorrect_user_test() {
        val response = UserService(repo).getUser(2)
        assertNull(response)
    }
}
