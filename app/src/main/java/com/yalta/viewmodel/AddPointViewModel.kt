package com.yalta.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.yalta.repositories.PointRepo
import com.yalta.repositories.RealPointRepo
import com.yalta.services.AddPointService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddPointViewModel(
    application: Application,
    repo: PointRepo = RealPointRepo(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): AndroidViewModel(application) {
    private var context: Context = getApplication<Application>().applicationContext
    private val _pointService = AddPointService(repo)

    val pointName = MutableLiveData<String>()
    val pointLat = MutableLiveData<String>()
    val pointLon = MutableLiveData<String>()
    val isCurrentPositionEnabled = MutableLiveData(false)
    val showEmptyFieldError = MutableLiveData(false)
    val showConnectionError = MutableLiveData(false)
    val closeActivity = MutableLiveData(false)

    fun createNewPoint() {
        showConnectionError(false)
        val name = pointName.value
        val lat = pointLat.value?.toDoubleOrNull()
        val lon = pointLon.value?.toDoubleOrNull()
        if (fieldsAreOK(name, lat, lon)) {
            viewModelScope.launch(dispatcher) {
                val res = _pointService.createNewPoint(name!!, lat!!, lon!!)
                if (!res) {
                    showConnectionError(true)
                } else {
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

    @SuppressLint("MissingPermission")
    fun setCurrentLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
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
