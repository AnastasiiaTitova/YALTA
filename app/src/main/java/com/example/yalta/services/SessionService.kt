package com.example.yalta.services

object SessionService {
    var session: Session? = null
        private set

    fun setSession(token: String) {
        session =
            Session(token)
    }

    fun discardSession() {
        session = null
    }

    fun isLoggedIn(): Boolean = session != null
}

class Session(val token: String)