package com.yalta.services

import com.yalta.repositories.PasswordRepo
import com.yalta.repositories.Reason
import com.yalta.repositories.process
import java.util.*

class PasswordService(private val repo: PasswordRepo) {
    suspend fun changePassword(newPassword: String): Optional<Boolean> {
        return process(
            { repo.changePassword(newPassword) },
            { Optional.of(true) },
            {
                if (it.reason == Reason.BAD_CODE) {
                    Optional.of(false)
                } else {
                    Optional.empty()
                }
            }
        )
    }
}
