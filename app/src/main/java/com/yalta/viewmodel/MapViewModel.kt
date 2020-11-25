package com.yalta.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.*
import com.google.android.gms.location.*
import com.yalta.repositories.LocationRepo
import com.yalta.repositories.RealLocationRepo
import com.yalta.services.LocationService
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

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location> = _currentLocation
    val locationPermissionsGranted = MutableLiveData<Boolean>()

    private val _locationService = LocationService(repo)

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
                    viewModelScope.launch(dispatcher) {
                        _locationService.sendCurrentLocation(locationResult.lastLocation)
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

    @SuppressLint("MissingPermission")
    private fun subscribe() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    @SuppressLint("MissingPermission")
    private fun unsubscribe() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}

class MapViewModelFactory(
    private val application: Application
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MapViewModel(application) as T
    }
}
