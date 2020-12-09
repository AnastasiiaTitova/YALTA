package com.yalta.repositories

import android.location.Location
import common.LocationUpdate

class AddedLocation(val location: common.Location) : SuccessfulResponse<AddedLocation>()

interface LocationRepo {
    suspend fun sendCurrentLocation(location: Location): RepoResponse<AddedLocation>
}

class RealLocationRepo : LocationRepo, RealRepo() {
    override suspend fun sendCurrentLocation(location: Location): RepoResponse<AddedLocation> {
        val body = common.Serialization.toJson(LocationUpdate(location.latitude, location.longitude))
        val response = doPostRequest("location/", body)

        return response.getRepoResponse { res ->
            AddedLocation(common.Serialization.fromJson(res.body()?.string()!!, common.Location::class.java))
        }
    }
}
