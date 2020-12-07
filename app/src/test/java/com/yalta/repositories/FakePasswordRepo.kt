package com.yalta.repositories

class FakePasswordRepo : PasswordRepo {
    private var canChangePassword = true

    override suspend fun changePassword(newPassword: String): RepoResponse<PasswordChanged> {
        val changed = canChangePassword
        canChangePassword = !canChangePassword

        return if (changed) {
            PasswordChanged()
        } else {
            FailedResponse()
        }
    }
}
