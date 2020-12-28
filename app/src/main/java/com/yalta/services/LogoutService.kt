package com.yalta.services

import com.yalta.repositories.LogoutRepo
import com.yalta.repositories.process
import javax.inject.Inject

class LogoutService @Inject constructor(private val repo: LogoutRepo) {
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
