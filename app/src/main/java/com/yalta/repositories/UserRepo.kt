package com.yalta.repositories

import common.User
import javax.inject.Inject

class GotUser(val user: User) : SuccessfulResponse<GotUser>()

interface UserRepo {
    suspend fun getUser(): RepoResponse<GotUser>
}

class RealUserRepo @Inject constructor(): UserRepo, RealRepo() {
    override suspend fun getUser(): RepoResponse<GotUser> {
        val response = doGetRequest("whoami")

        return response.getRepoResponse { res ->
            GotUser(common.Serialization.fromJson(res.body()?.string()!!, User::class.java))
        }
    }
}
