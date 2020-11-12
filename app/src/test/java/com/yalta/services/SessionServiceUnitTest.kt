package com.yalta.services

import org.junit.Test

import org.junit.Assert.*

class SessionServiceUnitTest {
    @Test
    fun init_test() {
        SessionService.setSession("token")
        assertNotNull(SessionService.session)
        assertTrue(SessionService.isLoggedIn())
    }

    @Test
    fun discard_test() {
        SessionService.discardSession()
        assertNull(SessionService.session)
        assertFalse(SessionService.isLoggedIn())
    }
}
