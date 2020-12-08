package com.yalta.repositories

import com.yalta.services.SessionService
import common.ChangePassword
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class PasswordChanged : SuccessfulResponse<PasswordChanged>()

interface PasswordRepo {
    suspend fun changePassword(newPassword: String): RepoResponse<PasswordChanged>
}

class RealPasswordRepo : PasswordRepo, RealRepo() {
    override suspend fun changePassword(newPassword: String): RepoResponse<PasswordChanged> {
        val url = "${baseUrl}/users/me/password"
        val client = OkHttpClient()
        val json = common.Serialization.toJson(ChangePassword(newPassword))
        val request = Request.Builder()
            .addHeader("Cookie", SessionService.session?.token!!)
            .url(url)
            .post(RequestBody
                .create(MediaType
                    .parse("text/plain"), json)
            )
            .build()
        val response = client.newCall(request).execute()

        return response.getRepoResponse {
            PasswordChanged()
        }
    }
}
