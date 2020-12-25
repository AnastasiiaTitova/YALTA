package com.yalta.viewmodel

import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yalta.R
import com.yalta.repositories.PointRepo
import com.yalta.repositories.RealPointRepo
import com.yalta.services.PointService
import com.yalta.utils.UniversalRecyclerItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminBrowseViewModel(
    pointRepo: PointRepo = RealPointRepo(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _pointService = PointService(pointRepo)
    val points = MutableLiveData<List<UniversalRecyclerItem>>()
    private val allPoints = mutableListOf<common.Point>()
    val search = MutableLiveData<String>()

    init {
        getAllPoints()
    }

    private fun getAllPoints() = viewModelScope.launch(dispatcher) {
        val receivedPoints = _pointService.getAllPoints()
        allPoints.addAll(receivedPoints)
        updateUIPoints(allPoints)
    }

    private fun common.Point.toRecyclerItem() =
        UniversalRecyclerItem(this, R.layout.point_details, BR.point)

    fun filterPoints() {
        if (search.value.isNullOrEmpty()) {
            updateUIPoints(allPoints)
            return
        }

        val filteredPoints = allPoints.filter { point ->
            point.name.contains(search.value.toString(), true)
        }.toMutableList()
        updateUIPoints(filteredPoints)
    }

    private fun updateUIPoints(newPoints: MutableList<common.Point>) = viewModelScope.launch(Dispatchers.Main) {
        points.value = newPoints.map {
            it.toRecyclerItem()
        }
    }

}
