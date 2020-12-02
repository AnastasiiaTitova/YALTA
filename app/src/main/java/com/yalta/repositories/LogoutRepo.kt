package com.yalta.repositories

import com.yalta.services.SessionService
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class LoggedOut : SuccessfulResponse<LoggedOut>()

interface LogoutRepo {
    suspend fun logout(): RepoResponse<LoggedOut>
}

class RealLogoutRepo : LogoutRepo, RealRepo() {
    override suspend fun logout(): RepoResponse<LoggedOut> {
        val url = "${baseUrl}/logout"
        val client = OkHttpClient()
        val request = Request.Builder()
            .addHeader("Cookie", SessionService.session?.token!!)
            .url(url)
            .post(RequestBody
                .create(MediaType
                    .parse("text/plain"), "")
            )
            .build()
        val response = client.newCall(request).execute()

        return response.getRepoResponse {
            LoggedOut()
        }
    }
}
