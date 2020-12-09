package com.yalta.repositories

import common.User

class GotUser(val user: User) : SuccessfulResponse<GotUser>()

interface UserRepo {
    suspend fun getUser(id: Long): RepoResponse<GotUser>
}

class RealUserRepo : UserRepo, RealRepo() {
    override suspend fun getUser(id: Long): RepoResponse<GotUser> {
        val response = doGetRequest("whoami")

        return response.getRepoResponse { res ->
            GotUser(common.Serialization.fromJson(res.body()?.string()!!, User::class.java))
        }
    }
}
