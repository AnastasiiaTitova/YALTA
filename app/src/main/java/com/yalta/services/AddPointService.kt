package com.yalta.services

import com.yalta.repositories.PointRepo
import com.yalta.repositories.process
import javax.inject.Inject

class AddPointService @Inject constructor(private val repo: PointRepo) {
    suspend fun createNewPoint(name: String, lat: Double, lon: Double): Boolean {
        return process(
            { repo.createNewPoint(name, lat, lon) },
            { true },
            { false }
        )
    }
}
