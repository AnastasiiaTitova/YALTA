package com.yalta.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import com.yalta.repositories.LocationRepo
import com.yalta.repositories.RealLocationRepo
import com.yalta.services.LocationService
import common.Route
import common.RoutePoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class MapViewModel(
    application: Application,
    repo: LocationRepo = RealLocationRepo(),
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AndroidViewModel(application) {
    private var context: Context = getApplication<Application>().applicationContext
    private var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationRequest: LocationRequest
    private var locationCallback: LocationCallback

    val locationPermissionsGranted = MutableLiveData<Boolean>()

    private val _locationService = LocationService(repo)

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

        viewModelScope.launch(dispatcher) {
            _currentRoute = _locationService.getCurrentRoute()
            updatePoints(_currentRoute?.points!!)
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                if (locationResult?.lastLocation != null) {
                    viewModelScope.launch(dispatcher) {
                        _locationService.sendCurrentLocation(locationResult.lastLocation)
                    }

                    if (_currentRoute != null) {
                        for (index in 0 until _currentRoute!!.points.size) {
                            if (!_currentRoute!!.points[index].visited) {
                                val results = FloatArray(1)
                                Location.distanceBetween(
                                    locationResult.lastLocation.latitude,
                                    locationResult.lastLocation.longitude,
                                    _currentRoute!!.points[index].point.lat,
                                    _currentRoute!!.points[index].point.lon,
                                    results)
                                if (results[0] <= 100) {
                                    val routeId = _currentRoute!!.id
                                    viewModelScope.launch(dispatcher) {
                                        val res = _locationService.updatePointState(routeId!!, index.toLong(), true)
                                        if (res) {
                                            _currentRoute = _locationService.getCurrentRoute()
                                            updatePoints(_currentRoute?.points!!)
                                        }
                                    }
                                }
                            }
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

    private fun updatePoints(points: List<RoutePoint>) = viewModelScope.launch(Dispatchers.Main) {
        _points.value = points
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
