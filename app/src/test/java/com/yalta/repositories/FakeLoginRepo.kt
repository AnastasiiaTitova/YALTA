package com.yalta.repositories

class FakeLoginRepo : LoginRepo {
    override suspend fun login(login: String, password: String): RepoResponse<SuccessfulLogin> {
        return if (login == "root" && password == "root")
            SuccessfulLogin("token")
        else
            FailedResponse()
    }
}
