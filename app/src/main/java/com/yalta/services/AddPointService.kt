package com.yalta.services

import com.yalta.repositories.PointRepo
import com.yalta.repositories.process

class AddPointService(private val repo: PointRepo) {
    suspend fun createNewPoint(name: String, lat: Double, lon: Double): Boolean {
        return process(
            { repo.createNewPoint(name, lat, lon) },
            { true },
            { false }
        )
    }
}
