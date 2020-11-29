package com.yalta.repositories

class FakeLoginRepo : LoginRepo {
    override suspend fun login(login: String, password: String): LoginResponse {
        return if (login == "root" && password == "root")
            SuccessfulLogin("token")
        else
            LoginFailed()
    }
}
