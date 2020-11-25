package com.yalta.repositories

import android.location.Location
import com.yalta.services.SessionService
import common.LocationUpdate
import okhttp3.*

sealed class LocationResponse
class AddedLocation(val location: common.Location) : LocationResponse()
class FailedLocation : LocationResponse()

interface LocationRepo {
    suspend fun sendCurrentLocation(location: Location): LocationResponse
}

class RealLocationRepo : LocationRepo, RealRepo() {
    override suspend fun sendCurrentLocation(location: Location): LocationResponse {
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

        return if (response.code() == 200) {
            AddedLocation(common.Serialization.fromJson(response.body()?.string()!!, common.Location::class.java))
        } else {
            FailedLocation()
        }
    }
}
