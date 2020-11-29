package com.yalta.repositories

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class SuccessfulLogin(val token: String) : SuccessfulResponse()

interface LoginRepo {
    suspend fun login(login: String, password: String): RepoResponse
}

class RealLoginRepo : LoginRepo, RealRepo() {
    override suspend fun login(login: String, password: String): RepoResponse {
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
            FailedResponse()
        }
    }
}