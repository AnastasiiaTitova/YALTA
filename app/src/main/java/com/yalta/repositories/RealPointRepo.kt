package com.yalta.repositories

import com.google.gson.reflect.TypeToken
import common.Point

class GotPoints(val points: List<Point>) : SuccessfulResponse<GotPoints>()

interface PointRepo {
    suspend fun getAllPoints(): RepoResponse<GotPoints>
}

class RealPointRepo : PointRepo, RealRepo() {
    override suspend fun getAllPoints(): RepoResponse<GotPoints> {
        val response = doGetRequest("points/")

        return response.getRepoResponse { res ->
            val type = TypeToken.getParameterized(List::class.java, Point::class.java).type
            GotPoints(common.Serialization.fromJson(res.body()?.string()!!, type))
        }
    }
}
