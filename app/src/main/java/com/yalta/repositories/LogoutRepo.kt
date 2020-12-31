package com.yalta.repositories

import javax.inject.Inject

class LoggedOut : SuccessfulResponse<LoggedOut>()

interface LogoutRepo {
    suspend fun logout(): RepoResponse<LoggedOut>
}

class RealLogoutRepo @Inject constructor() : LogoutRepo, RealRepo() {
    override suspend fun logout(): RepoResponse<LoggedOut> {
        val response = doPostRequest("logout", "")

        return response.getRepoResponse {
            LoggedOut()
        }
    }
}
