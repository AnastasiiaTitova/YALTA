package com.yalta.services

import com.yalta.repositories.PointRepo
import com.yalta.repositories.process
import common.Point
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointService @Inject constructor(private val repo: PointRepo) {
    suspend fun getAllPoints(): List<Point> {
        return process(
            { repo.getAllPoints() },
            { it.points },
            { emptyList() }
        )
    }

    suspend fun createNewPoint(name: String, lat: Double, lon: Double): Boolean {
        return process(
            { repo.createNewPoint(name, lat, lon) },
            { true },
            { false }
        )
    }
}
