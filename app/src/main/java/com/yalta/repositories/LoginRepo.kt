package com.yalta.repositories

class SuccessfulLogin(val token: String) : SuccessfulResponse<SuccessfulLogin>()

interface LoginRepo {
    suspend fun login(login: String, password: String): RepoResponse<SuccessfulLogin>
}

class RealLoginRepo : LoginRepo, RealRepo() {
    override suspend fun login(login: String, password: String): RepoResponse<SuccessfulLogin> {
        val response = doLoginRequest(login, password)

        return response.getRepoResponse { res ->
            SuccessfulLogin(
                res.header("Set-Cookie")
                    .toString()
                    .substringBefore(';')
            )
        }
    }
}
