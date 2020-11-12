package com.yalta.services

import common.Admin
import common.Driver
import common.User

sealed class UserResponse
class GotUser(val user: User) : UserResponse()
class NoUser : UserResponse()

class UserService(private val repo: UserRepo) {
    fun getUser(id: Long = 1): User? {
        return when (val res = repo.getUser(id)) {
            is NoUser -> null
            is GotUser -> res.user
        }
    }

    fun changeUser(id: Long): User? {
        return repo.changeUser(id)
    }
}

interface UserRepo {
    fun getUser(id: Long): UserResponse
    fun changeUser(id: Long): User?
}

class HardcodedUserRepo : UserRepo {
    private var user = User(1, "root", "root", Driver)

    override fun getUser(id: Long): UserResponse {
        return if (user.id != id) NoUser() else GotUser(user)
    }

    override fun changeUser(id: Long): User? {
        user = User(user.id, user.name, user.password, if (user.role is Driver) Admin else Driver)
        return user
    }
}
