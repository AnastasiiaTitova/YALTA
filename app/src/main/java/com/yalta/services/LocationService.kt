package com.yalta.services

import android.location.Location
import com.yalta.repositories.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationService @Inject constructor(private val repo: LocationRepo) {
    suspend fun sendCurrentLocation(location: Location): common.Location? {
        return process(
            { repo.sendCurrentLocation(location) },
            { it.location },
            { null }
        )
    }

    suspend fun updatePointState(routeId: Long, pointId: Long, visited: Boolean): Boolean {
        return process(
            { repo.updatePointState(routeId, pointId, visited) },
            { true },
            { false }
        )
    }
}
