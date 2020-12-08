package com.yalta.services

import com.yalta.repositories.PasswordRepo
import com.yalta.repositories.process

class PasswordService(private val repo: PasswordRepo) {
    suspend fun changePassword(newPassword: String): Boolean {
        return process(
            { repo.changePassword(newPassword) },
            { true },
            { false }
        )
    }
}
