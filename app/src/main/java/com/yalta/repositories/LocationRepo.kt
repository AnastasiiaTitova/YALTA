package com.yalta.repositories

import android.location.Location
import com.yalta.services.SessionService
import common.LocationUpdate
import okhttp3.*

class AddedLocation(val location: common.Location) : SuccessfulResponse<AddedLocation>()

interface LocationRepo {
    suspend fun sendCurrentLocation(location: Location): RepoResponse<AddedLocation>
}

class RealLocationRepo : LocationRepo, RealRepo() {
    override suspend fun sendCurrentLocation(location: Location): RepoResponse<AddedLocation> {
        val url = "${baseUrl}/location/"
        val client = OkHttpClient()
        val json = common.Serialization.toJson(LocationUpdate(location.latitude, location.longitude))
        val body = RequestBody.create(MediaType.parse("text/plain"), json)
        val request = Request.Builder()
            .addHeader("Cookie", SessionService.session?.token!!)
            .url(url)
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        return response.getRepoResponse { res ->
            AddedLocation(common.Serialization.fromJson(res.body()?.string()!!, common.Location::class.java))
        }
    }
}
