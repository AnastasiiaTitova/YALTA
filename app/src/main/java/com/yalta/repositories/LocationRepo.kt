package com.yalta.repositories

import android.location.Location
import common.LocationUpdate
import common.UpdatePoint
import javax.inject.Inject

class AddedLocation(val location: common.Location) : SuccessfulResponse<AddedLocation>()
class UpdatedPoint : SuccessfulResponse<UpdatedPoint>()

interface LocationRepo {
    suspend fun sendCurrentLocation(location: Location): RepoResponse<AddedLocation>
    suspend fun updatePointState(routeId: Long, pointId: Long, visited: Boolean) : RepoResponse<UpdatedPoint>
}

class RealLocationRepo @Inject constructor(): LocationRepo, RealRepo() {
    override suspend fun sendCurrentLocation(location: Location): RepoResponse<AddedLocation> {
        val body = common.Serialization.toJson(LocationUpdate(location.latitude, location.longitude))
        val response = doPostRequest("location/", body)

        return response.getRepoResponse { res ->
            AddedLocation(common.Serialization.fromJson(res.body()?.string()!!, common.Location::class.java))
        }
    }

    override suspend fun updatePointState(routeId: Long, pointId: Long, visited: Boolean): RepoResponse<UpdatedPoint> {
        val body = common.Serialization.toJson(UpdatePoint(visited))
        val response = doPutRequest("routes/${routeId}/points/${pointId}", body)

        return response.getRepoResponse {
            UpdatedPoint()
        }
    }
}
