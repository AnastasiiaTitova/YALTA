package com.yalta.repositories

import com.google.gson.reflect.TypeToken
import common.Point
import javax.inject.Inject

class GotPoints(val points: List<Point>) : SuccessfulResponse<GotPoints>()
class PointCreated : SuccessfulResponse<PointCreated>()

interface PointRepo {
    suspend fun getAllPoints(): RepoResponse<GotPoints>
    suspend fun createNewPoint(name: String, lat: Double, lon: Double): RepoResponse<PointCreated>
}

class RealPointRepo @Inject constructor() : PointRepo, RealRepo() {
    override suspend fun getAllPoints(): RepoResponse<GotPoints> {
        val response = doGetRequest("points/")

        return response.getRepoResponse { res ->
            val type = TypeToken.getParameterized(List::class.java, Point::class.java).type
            GotPoints(common.Serialization.fromJson(res.body()?.string()!!, type))
        }
    }

    override suspend fun createNewPoint(name: String, lat: Double, lon: Double): RepoResponse<PointCreated> {
        val body = common.Serialization.toJson(Point(null, lat, lon, name))
        val response = doPostRequest("points/", body)

        return response.getRepoResponse { PointCreated() }
    }
}
