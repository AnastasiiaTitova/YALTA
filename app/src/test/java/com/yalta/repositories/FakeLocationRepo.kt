package com.yalta.repositories

import android.location.Location
import java.sql.Timestamp

class FakeLocationRepo : LocationRepo {
    private var locations: MutableList<Pair<Double, Double>> = mutableListOf()

    override suspend fun sendCurrentLocation(location: Location): RepoResponse<AddedLocation> {
        locations.add(Pair(location.latitude, location.longitude))
        return AddedLocation(
            common.Location(
                null,
                locations.last().first,
                locations.last().second,
                1,
                Timestamp(System.currentTimeMillis())
            )
        )
    }
}
