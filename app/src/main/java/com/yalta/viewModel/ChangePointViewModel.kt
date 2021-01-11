package com.yalta.viewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.yalta.di.YaltaApplication
import com.yalta.services.PointService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangePointViewModel @Inject constructor(
    private val pointService: PointService,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private var context: Context = YaltaApplication.context
    private var fusedLocationProviderClient: FusedLocationProviderClient?

    val pointName = MutableLiveData<String>()
    val pointLat = MutableLiveData<String>()
    val pointLon = MutableLiveData<String>()
    val isCurrentPositionEnabled = MutableLiveData(true)
    val showEmptyFieldError = MutableLiveData(false)
    val showConnectionError = MutableLiveData(false)
    val closeActivity = MutableLiveData(false)
    lateinit var oldPoint: common.Point
    var arePermissionsGranted: () -> Boolean = { false }
    var requestPermissions: () -> Unit = { }

    init {
        fusedLocationProviderClient =
            if (arePermissionsGranted()) {
                getLocationProvider()
            } else {
                null
            }
    }

    fun updatePoint() {
        showConnectionError(false)
        val name = pointName.value
        val lat = pointLat.value?.toDoubleOrNull()
        val lon = pointLon.value?.toDoubleOrNull()
        if (fieldsAreOK(name, lat, lon)) {
            viewModelScope.launch(dispatcher) {
                var res: Boolean? = null
                if (oldPoint.name != name) {
                    res = pointService.updatePointName(oldPoint.id!!, name!!, lat!!, lon!!)
                    if (!res) {
                        showConnectionError(true)
                    }
                }
                if (oldPoint.lat != lat || oldPoint.lon != lon) {
                    res = pointService.updatePointPosition(oldPoint.id!!, name, lat!!, lon!!)
                    if (!res) {
                        showConnectionError(true)
                    }
                }
                if (res == true) {
                    closeActivity()
                }
            }
        } else {
            showEmptyFieldError(true)
        }
    }

    private fun fieldsAreOK(name: String?, lat: Double?, lon: Double?): Boolean {
        if (name.isNullOrEmpty() || lat == null || lon == null) {
            return false
        }
        return true
    }

    fun showEmptyFieldError(value: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        showEmptyFieldError.value = value
    }

    private fun showConnectionError(value: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        showConnectionError.value = value
    }

    fun getLocationProvider(): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun setCurrentLocation() {
        if (!arePermissionsGranted()) {
            requestPermissions()
            return
        }
        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = getLocationProvider()
        }
        fusedLocationProviderClient?.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
            ?.addOnSuccessListener { location ->
                if (location != null) {
                    pointLat.value = location.latitude.toString()
                    pointLon.value = location.longitude.toString()
                }
            }
    }

    private fun closeActivity() = viewModelScope.launch(Dispatchers.Main) {
        closeActivity.value = true
    }
}
