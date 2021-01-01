package com.yalta.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import com.yalta.di.YaltaApplication
import com.yalta.services.LocationService
import com.yalta.services.RoutesService
import common.Route
import common.RoutePoint
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MapViewModel @Inject constructor(
    private val locationService: LocationService,
    private val routesService: RoutesService,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private var context: Context = YaltaApplication.context
    private var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationRequest: LocationRequest
    private var locationCallback: LocationCallback

    val locationPermissionsGranted = MutableLiveData<Boolean>()


    private var _currentRoute: Route? = null
    private val _points = MutableLiveData<List<RoutePoint>>()
    val points: LiveData<List<RoutePoint>> = _points

    init {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(15)
            fastestInterval = TimeUnit.SECONDS.toMillis(5)
            maxWaitTime = TimeUnit.MINUTES.toMillis(5)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        updateRoute()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                if (locationResult?.lastLocation != null) {
                    viewModelScope.launch(dispatcher) {
                        locationService.sendCurrentLocation(locationResult.lastLocation)
                    }

                    if (_currentRoute != null) {
                        val somePointsChanged = maybeUpdatePoints(locationResult.lastLocation)

                        if (somePointsChanged) {
                            updateRoute()
                        }
                    }
                }
            }
        }

        locationPermissionsGranted.observeForever { value: Boolean ->
            if (value) {
                subscribe()
            } else {
                unsubscribe()
            }
        }
    }

    private fun updateRoute() = viewModelScope.launch(dispatcher) {
        _currentRoute = routesService.getCurrentRoute()
        if (_currentRoute != null) {
            updatePoints(_currentRoute?.points!!)
        }
    }

    private fun updatePoints(points: List<RoutePoint>) = viewModelScope.launch(Dispatchers.Main) {
        _points.value = points
    }

    private fun calculateDistance(point: RoutePoint, currentLocation: Location): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            currentLocation.latitude,
            currentLocation.longitude,
            point.point.lat,
            point.point.lon,
            results
        )

        return results[0]
    }

    private fun maybeUpdatePoints(currentLocation: Location): Boolean {
        val changedPoints = _currentRoute?.points?.filter { point ->
            !point.visited && calculateDistance(point, currentLocation) <= 100
        }

        if (changedPoints.isNullOrEmpty()) {
            return false
        }

        val routeId = _currentRoute?.id!!
        viewModelScope.launch {
            changedPoints.map { point ->
                viewModelScope.async(dispatcher) {
                    locationService.updatePointState(routeId, point.index, true)
                }
            }.awaitAll()
        }
        return true
    }

    @SuppressLint("MissingPermission")
    private fun subscribe() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    @SuppressLint("MissingPermission")
    private fun unsubscribe() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}
