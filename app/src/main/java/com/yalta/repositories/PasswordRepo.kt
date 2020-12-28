package com.yalta.repositories

import common.ChangePassword
import javax.inject.Inject

class PasswordChanged : SuccessfulResponse<PasswordChanged>()

interface PasswordRepo {
    suspend fun changePassword(newPassword: String): RepoResponse<PasswordChanged>
}

class RealPasswordRepo @Inject constructor() : PasswordRepo, RealRepo() {
    override suspend fun changePassword(newPassword: String): RepoResponse<PasswordChanged> {
        val body = common.Serialization.toJson(ChangePassword(newPassword))
        val response = doPostRequest("users/me/password", body)

        return response.getRepoResponse {
            PasswordChanged()
        }
    }
}
