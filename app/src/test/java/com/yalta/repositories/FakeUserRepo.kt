package com.yalta.repositories

import common.Admin
import common.Driver
import common.User

class FakeUserRepo : UserRepo {
    private var user = User(1, "root", "root", Driver)

    override suspend fun getUser(id: Long): UserResponse {
        return if (user.id != id) NoUser() else GotUser(user)
    }

    override suspend fun changeUser(id: Long): User? {
        user = User(user.id, user.name, user.password, if (user.role is Driver) Admin else Driver)
        return user
    }
}
