package com.yalta.utils

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
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
}
