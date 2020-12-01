package com.yalta.services

import com.yalta.repositories.LogoutRepo
import com.yalta.repositories.process

class LogoutService(private val repo: LogoutRepo) {
    suspend fun logout(): Boolean {
        return process(
            { repo.logout() },
            {
                SessionService.discardSession()
                true
            },
            { false }
        )
    }
}
