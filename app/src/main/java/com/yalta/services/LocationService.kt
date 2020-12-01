package com.yalta.services

import android.location.Location
import com.yalta.repositories.*

class LocationService(private val repo: LocationRepo) {
    suspend fun sendCurrentLocation(location: Location): common.Location? {
        return process(
            { repo.sendCurrentLocation(location) },
            { it.location },
            { null }
        )
    }
}
