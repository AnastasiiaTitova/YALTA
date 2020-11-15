package com.yalta.repositories

import com.yalta.services.SessionService
import common.Admin
import common.Driver
import common.User
import okhttp3.OkHttpClient
import okhttp3.Request

sealed class UserResponse
class GotUser(val user: User) : UserResponse()
class NoUser : UserResponse()

interface UserRepo {
    suspend fun getUser(id: Long): UserResponse
    suspend fun changeUser(id: Long): User?
}

class FakeUserRepo : UserRepo {
    private var user = User(1, "root", "root", Driver)

    override suspend fun getUser(id: Long): UserResponse {
        return if (user.id != id) NoUser() else GotUser(user)
    }

    override suspend fun changeUser(id: Long): User? {
        user = User(user.id, user.name, user.password, if (user.role is Driver) Admin else Driver)
        return user
    }
}

class RealUserRepo : UserRepo, RealRepo() {
    override suspend fun getUser(id: Long): UserResponse {
        val url = "${baseUrl}/whoami"
        val client = OkHttpClient()
        val request = Request.Builder()
            .addHeader("Cookie", SessionService.session?.token!!)
            .url(url)
            .build()
        val response = client.newCall(request).execute()

        return if (response.code() == 200) {
            GotUser(common.Serialization.fromJson(response.body()?.string()!!, User::class.java))
        } else {
            NoUser()
        }
    }

    override suspend fun changeUser(id: Long): User? {
        TODO("NIY")
    }
}