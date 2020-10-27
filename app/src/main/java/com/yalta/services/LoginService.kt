package com.yalta.services

sealed class LoginResponse
class SuccessfulLogin(val token: String) : LoginResponse()
class LoginFailed : LoginResponse()

class LoginService(private val repo: LoginRepo, private val session: SessionService) {
    fun login(login: String, password: String): Boolean {
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

interface LoginRepo {
    fun login(login: String, password: String): LoginResponse
}

class HardcodedLocalRepo : LoginRepo {
    override fun login(login: String, password: String): LoginResponse {
        return if (login == "root" && password == "root")
            SuccessfulLogin("hardcodedtoken")
        else
            LoginFailed()
    }
}
