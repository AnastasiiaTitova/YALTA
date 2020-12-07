package com.yalta.repositories

class FakePasswordRepo : PasswordRepo {
    override suspend fun changePassword(newPassword: String): RepoResponse<PasswordChanged> {
        return PasswordChanged()
    }
}
