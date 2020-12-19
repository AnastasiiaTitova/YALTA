package com.yalta.services

import com.yalta.repositories.UserRepo
import com.yalta.repositories.process
import common.User

class UserService(private val repo: UserRepo) {
    suspend fun getUser(): User? {
        return process(
            { repo.getUser() },
            { it.user },
            { null }
        )
    }
}
