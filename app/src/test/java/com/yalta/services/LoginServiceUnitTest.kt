package com.yalta.services

import org.junit.Test

import org.junit.Assert.*

class LoginServiceUnitTest {
    private val repo = HardcodedLocalRepo()

    @Test
    fun correct_credentials_test() {
        assertTrue(LoginService(repo, SessionService).login("root", "root"))
    }

    @Test
    fun wrong_credentials_test() {
        assertFalse(LoginService(repo, SessionService).login("user", "password"))
    }
}
