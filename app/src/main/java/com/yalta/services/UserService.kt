package com.yalta.services

import com.yalta.repositories.GotUser
import com.yalta.repositories.NoUser
import com.yalta.repositories.UserRepo
import common.User

class UserService(private val repo: UserRepo) {
    suspend fun getUser(id: Long = 1): User? {
        return when (val res = repo.getUser(id)) {
            is NoUser -> null
            is GotUser -> res.user
        }
    }

    suspend fun changeUser(id: Long): User? {
        return repo.changeUser(id)
    }
}
