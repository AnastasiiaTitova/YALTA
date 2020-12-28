package com.yalta.services

import com.yalta.repositories.PointRepo
import com.yalta.repositories.process
import common.Point
import javax.inject.Inject

class PointService @Inject constructor(private val repo: PointRepo) {
    suspend fun getAllPoints(): List<Point> {
        return process(
            { repo.getAllPoints() },
            { it.points },
            { emptyList() }
        )
    }
}
