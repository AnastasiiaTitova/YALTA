package com.yalta.services

import com.yalta.repositories.LoginFailed
import com.yalta.repositories.LoginRepo
import com.yalta.repositories.SuccessfulLogin

class LoginService(private val repo: LoginRepo) {
    suspend fun login(login: String, password: String): Boolean {
        return when (val res = repo.login(login, password)) {
            is SuccessfulLogin -> {
                SessionService.setSession(res.token)
                true
            }
            is LoginFailed -> {
                SessionService.discardSession()
                false
            }
        }
    }
}
