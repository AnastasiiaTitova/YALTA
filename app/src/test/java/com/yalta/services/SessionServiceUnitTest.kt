package com.yalta.services

import org.junit.Test

import org.junit.Assert.*

class SessionServiceUnitTest {
    @Test
    fun initTest() {
        SessionService.setSession("token")
        assertNotNull(SessionService.session)
        assertTrue(SessionService.isLoggedIn())
    }

    @Test
    fun discardTest() {
        SessionService.discardSession()
        assertNull(SessionService.session)
        assertFalse(SessionService.isLoggedIn())
    }
}
