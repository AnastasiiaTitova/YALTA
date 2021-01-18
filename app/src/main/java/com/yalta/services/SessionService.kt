package com.yalta.services

import common.Role

object SessionService {
    var session: Session? = null
        private set

    fun setSession(token: String, role: Role) {
        session =
            Session(token, role)
    }

    fun discardSession() {
        session = null
    }

    fun isLoggedIn(): Boolean = session != null
}

class Session(val token: String, val role: Role)
