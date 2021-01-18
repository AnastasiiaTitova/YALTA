package com.yalta.repositories

import com.google.gson.reflect.TypeToken
import common.Point
import javax.inject.Inject

class GotPoints(val points: List<Point>) : SuccessfulResponse<GotPoints>()
class PointCreated : SuccessfulResponse<PointCreated>()
class PointUpdated : SuccessfulResponse<PointUpdated>()

interface PointRepo {
    suspend fun getAllPoints(): RepoResponse<GotPoints>
    suspend fun createNewPoint(name: String, lat: Double, lon: Double): RepoResponse<PointCreated>
    suspend fun updatePointName(id: Long, name: String, lat: Double, lon: Double): RepoResponse<PointUpdated>
    suspend fun updatePointPosition(id: Long, name: String, lat: Double, lon: Double): RepoResponse<PointUpdated>
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

    override suspend fun updatePointName(id: Long, name: String, lat: Double, lon: Double): RepoResponse<PointUpdated> {
        val body = common.Serialization.toJson(Point(id, lat, lon, name))
        val response = doPutRequest("points/${id}/name", body)

        return response.getRepoResponse { PointUpdated() }
    }

    override suspend fun updatePointPosition(id: Long, name: String, lat: Double, lon: Double): RepoResponse<PointUpdated> {
        val body = common.Serialization.toJson(Point(id, lat, lon, name))
        val response = doPutRequest("points/${id}/location", body)

        return response.getRepoResponse { PointUpdated() }
    }
}
