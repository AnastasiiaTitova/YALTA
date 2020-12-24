package com.yalta.services

import com.yalta.repositories.*
import java.util.*

class LoginService(private val repo: LoginRepo) {
    suspend fun login(login: String, password: String): Optional<Boolean> {
        return process(
            { repo.login(login, password) },
            { sLogin ->
                SessionService.setSession(sLogin.token, sLogin.role)
                Optional.of(true)
            },
            {
                SessionService.discardSession()
                if (it.reason == Reason.BAD_CODE) {
                    Optional.of(false)
                } else {
                    Optional.empty()
                }
            }
        )
    }
}
