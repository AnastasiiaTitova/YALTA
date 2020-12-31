package com.yalta.services

import com.yalta.repositories.UserRepo
import com.yalta.repositories.process
import common.User
import javax.inject.Inject

class UserService @Inject constructor(private val repo: UserRepo) {
    suspend fun getUser(): User? {
        return process(
            { repo.getUser() },
            { it.user },
            { null }
        )
    }
}
