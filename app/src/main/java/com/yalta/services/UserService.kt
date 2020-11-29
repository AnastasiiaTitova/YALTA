package com.yalta.services

import com.yalta.repositories.GotUser
import com.yalta.repositories.UserRepo
import com.yalta.repositories.process
import common.User

class UserService(private val repo: UserRepo) {
    suspend fun getUser(id: Long = 1): User? {
        return process<GotUser, User?>(
            { repo.getUser(id) },
            { it.user },
            { null }
        )
    }

    suspend fun changeUser(id: Long): User? {
        return repo.changeUser(id)
    }
}
