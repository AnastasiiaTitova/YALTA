package com.yalta.repositories

import common.Admin
import common.Driver
import common.User

class FakeUserRepo : UserRepo {
    private var user = User(1, "root", "root", Driver)

    override suspend fun getUser(id: Long): RepoResponse<GotUser> {
        return if (user.id != id) FailedResponse() else GotUser(user)
    }
}
