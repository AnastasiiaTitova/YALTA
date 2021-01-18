package com.yalta.utils

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import common.RoutePoint

object MapUtils {
    fun List<RoutePoint>.convertToMarkerOptions(): MutableList<MarkerOptions> =
        this.map { point ->
            val color = if (point.visited) BitmapDescriptorFactory.HUE_GREEN else BitmapDescriptorFactory.HUE_RED
            MarkerOptions()
                .position(LatLng(point.point.lat, point.point.lon))
                .title(point.point.name)
                .draggable(false)
                //.snippet() <- maybe put here info about items
                .icon(BitmapDescriptorFactory.defaultMarker(color))
        }.toMutableList()

    fun List<RoutePoint>.convertToBounds(): LatLngBounds? {
        if (this.isEmpty()) {
            return null
        }

        val lat = this.map { routePoint -> routePoint.point.lat }.toDoubleArray()
        val lon = this.map { routePoint -> routePoint.point.lon }.toDoubleArray()

        val north = lat.maxOrNull()
        val south = lat.minOrNull()
        val east = lon.maxOrNull()
        val west = lon.minOrNull()

        return LatLngBounds(LatLng(south!!, west!!), LatLng(north!!, east!!))
    }
}
