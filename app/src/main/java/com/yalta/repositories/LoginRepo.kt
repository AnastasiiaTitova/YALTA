package com.yalta.repositories

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

sealed class LoginResponse
class SuccessfulLogin(val token: String) : LoginResponse()
class LoginFailed : LoginResponse()

interface LoginRepo {
    suspend fun login(login: String, password: String): LoginResponse
}

class FakeLoginRepo : LoginRepo {
    override suspend fun login(login: String, password: String): LoginResponse {
        return if (login == "root" && password == "root")
            SuccessfulLogin("token")
        else
            LoginFailed()
    }
}

class RealLoginRepo : LoginRepo, RealRepo() {
    override suspend fun login(login: String, password: String): LoginResponse {
        val url = "${baseUrl}/login?username=$login&password=$password"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(
                FormBody.Builder()
                    .build()
            )
            .build()

        val response = client.newCall(request).execute()

        return if (response.code() == 200) {
            SuccessfulLogin(
                response.header("Set-Cookie")
                    .toString()
                    .substringBefore(';')
            )
        } else {
            LoginFailed()
        }
    }
}