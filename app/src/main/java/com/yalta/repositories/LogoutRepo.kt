package com.yalta.repositories

class LoggedOut : SuccessfulResponse<LoggedOut>()

interface LogoutRepo {
    suspend fun logout(): RepoResponse<LoggedOut>
}

class RealLogoutRepo : LogoutRepo, RealRepo() {
    override suspend fun logout(): RepoResponse<LoggedOut> {
        val response = doPostRequest("logout", "")

        return response.getRepoResponse {
            LoggedOut()
        }
    }
}
