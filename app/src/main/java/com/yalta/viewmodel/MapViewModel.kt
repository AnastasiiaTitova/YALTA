package com.yalta.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private var context: Context = getApplication<Application>().applicationContext
    private var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationRequest: LocationRequest
    private var locationCallback: LocationCallback

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location> = _currentLocation
    val locationPermissionsGranted = MutableLiveData<Boolean>()

    init {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(15)
            fastestInterval = TimeUnit.SECONDS.toMillis(5)
            maxWaitTime = TimeUnit.MINUTES.toMillis(5)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                if (locationResult?.lastLocation != null) {
                    _currentLocation.value = locationResult.lastLocation
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

    @SuppressLint("MissingPermission")
    private fun subscribe() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    @SuppressLint("MissingPermission")
    private fun unsubscribe() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}