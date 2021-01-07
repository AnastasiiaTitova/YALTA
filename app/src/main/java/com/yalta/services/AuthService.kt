package com.yalta.services

import com.yalta.repositories.*
import common.User
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService @Inject constructor(private val repo: AuthRepo, private val storage: Storage) {
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

    suspend fun logout(): Boolean {
        return process(
            { repo.logout() },
            {
                storage.clearData()
                SessionService.discardSession()
                true
            },
            { false }
        )
    }

    suspend fun getUser(): User? {
        return process(
            { repo.getUser() },
            { it.user },
            { null }
        )
    }
}
