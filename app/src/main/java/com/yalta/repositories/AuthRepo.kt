package com.yalta.repositories

import common.Role
import common.User
import javax.inject.Inject
import javax.inject.Singleton

class GotUser(val user: User) : SuccessfulResponse<GotUser>()

class LoggedOut : SuccessfulResponse<LoggedOut>()

class SuccessfulLogin(val token: String, val role: Role) : SuccessfulResponse<SuccessfulLogin>()

interface AuthRepo {
    suspend fun login(login: String, password: String): RepoResponse<SuccessfulLogin>
    suspend fun logout(): RepoResponse<LoggedOut>
    suspend fun getUser(): RepoResponse<GotUser>
}

@Singleton
class RealAuthRepo @Inject constructor() : AuthRepo, RealRepo() {
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

    override suspend fun logout(): RepoResponse<LoggedOut> {
        val response = doPostRequest("logout", "")

        return response.getRepoResponse {
            LoggedOut()
        }
    }

    override suspend fun getUser(): RepoResponse<GotUser> {
        val response = doGetRequest("whoami")

        return response.getRepoResponse { res ->
            GotUser(common.Serialization.fromJson(res.body()?.string()!!, User::class.java))
        }
    }
}
