package com.yalta.services

import com.yalta.repositories.RoutesRepo
import com.yalta.repositories.process
import common.Route
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoutesService @Inject constructor(private val repo: RoutesRepo) {
    suspend fun getRoutes(from: String, to: String): Optional<List<Route>> {
        return process(
            { repo.getRoutes(from, to) },
            { Optional.of(it.routes) },
            { Optional.empty() }
        )
    }

    suspend fun getCurrentRoute(): Route? {
        return process(
            { repo.getCurrentRoute() },
            { it.route },
            { null }
        )
    }
}
