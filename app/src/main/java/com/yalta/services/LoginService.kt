package com.yalta.services

import com.yalta.repositories.*

class LoginService(private val repo: LoginRepo) {
    suspend fun login(login: String, password: String): Boolean {
        return process(
            { repo.login(login, password) },
            { sLogin ->
                SessionService.setSession(sLogin.token)
                true
            },
            {
                SessionService.discardSession()
                false
            }
        )
    }
}
