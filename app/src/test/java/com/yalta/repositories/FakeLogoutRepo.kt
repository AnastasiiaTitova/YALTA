package com.yalta.repositories

class FakeLogoutRepo : LogoutRepo {
    private var _loggedOut = false

    override suspend fun logout(): RepoResponse<LoggedOut> {
        _loggedOut = !_loggedOut
        return if (_loggedOut) {
            LoggedOut()
        } else {
            FailedResponse(Reason.BAD_CODE)
        }
    }
}
