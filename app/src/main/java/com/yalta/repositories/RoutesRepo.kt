package com.yalta.repositories

import com.google.gson.reflect.TypeToken
import common.Route
import javax.inject.Inject

class GotRoutes(val routes: List<Route>) : SuccessfulResponse<GotRoutes>()

interface RoutesRepo {
    suspend fun getRoutes(from: String, to: String): RepoResponse<GotRoutes>
}

class RealRoutesRepo @Inject constructor(): RoutesRepo, RealRepo() {
    override suspend fun getRoutes(from: String, to: String): RepoResponse<GotRoutes> {
        val response = doGetRequest("routes/", listOf(Pair("from", from), Pair("to", to)))

        return response.getRepoResponse { res ->
            val type = TypeToken.getParameterized(List::class.java, Route::class.java).type
            GotRoutes(common.Serialization.fromJson(res.body()?.string()!!, type))
        }
    }
}
