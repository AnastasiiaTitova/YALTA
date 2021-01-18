package com.yalta.repositories

import com.google.gson.reflect.TypeToken
import common.Route
import javax.inject.Inject

class GotRoute(val route: Route) : SuccessfulResponse<GotRoute>()
class GotRoutes(val routes: List<Route>) : SuccessfulResponse<GotRoutes>()

interface RoutesRepo {
    suspend fun getCurrentRoute() : RepoResponse<GotRoute>
    suspend fun getRoutes(from: String, to: String): RepoResponse<GotRoutes>
}

class RealRoutesRepo @Inject constructor(): RoutesRepo, RealRepo() {
    override suspend fun getCurrentRoute() : RepoResponse<GotRoute> {
        val response = doGetRequest("routes/current")

        return response.getRepoResponse { res ->
            GotRoute(common.Serialization.fromJson(res.body()?.string()!!, Route::class.java))
        }
    }

    override suspend fun getRoutes(from: String, to: String): RepoResponse<GotRoutes> {
        val response = doGetRequest("routes/", listOf(Pair("from", from), Pair("to", to)))

        return response.getRepoResponse { res ->
            val type = TypeToken.getParameterized(List::class.java, Route::class.java).type
            GotRoutes(common.Serialization.fromJson(res.body()?.string()!!, type))
        }
    }
}
