package com.yalta.repositories

import common.Role
import common.User
import javax.inject.Inject

class SuccessfulLogin(val token: String, val role: Role) : SuccessfulResponse<SuccessfulLogin>()

interface LoginRepo {
    suspend fun login(login: String, password: String): RepoResponse<SuccessfulLogin>
}

class RealLoginRepo @Inject constructor(): LoginRepo, RealRepo() {
    override suspend fun login(login: String, password: String): RepoResponse<SuccessfulLogin> {
        val response = doLoginRequest(login, password)

        return response.getRepoResponse { res ->
            SuccessfulLogin(
                res.header("Set-Cookie")
                    .toString()
                    .substringBefore(';'),
                common.Serialization.fromJson(res.body()?.string()!!, User::class.java).role!!
            )
        }
    }
}
