package com.yalta.viewmodel

import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yalta.R
import com.yalta.services.RoutesService
import com.yalta.utils.UniversalRecyclerItem
import common.Route
import common.RoutePoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import java.util.*
import javax.inject.Inject

class DriverRoutesViewModel @Inject constructor(
    private val routesService: RoutesService,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    var routes: List<Route>? = emptyList()
    val routesNames = MutableLiveData<List<String>>()
    var selectedRoute: Int? = null
    val selectedRoutePoints = MutableLiveData<List<UniversalRecyclerItem>>()
    val fromDate = MutableLiveData<DateTime>()
    val toDate = MutableLiveData<DateTime>()
    val showDatesError = MutableLiveData(false)

    fun getSomeRoutes() {
        var from = fromDate.value
        var to = toDate.value
        val timezone = DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Moscow"))

        from = from ?: DateTime.now(timezone)
        from = from!!.withHourOfDay(0).withMinuteOfHour(0)
        to = to ?: DateTime.now(timezone)
        to = to!!.withHourOfDay(23).withMinuteOfHour(59)

        if (datesAreOk(from, to)) {
            getSelectedRoutes(from!!, to!!)
        } else {
            showDatesError(true)
        }
    }

    private fun datesAreOk(from: DateTime?, to: DateTime?): Boolean =
        from != null && to != null && from <= to

    private fun getSelectedRoutes(from: DateTime, to: DateTime) = viewModelScope.launch(dispatcher) {
        routes = routesService.getRoutes(from.toString(), to.toString())
        if (routes != null) {
            setRoutes()
        }
    }

    private fun setRoutes() = viewModelScope.launch(Dispatchers.Main) {
        routesNames.value = routes?.map { route ->
            "ID: ${route.id} Date: ${route.routeDate.toString(DateTimeFormat.forPattern("dd-MM-yy HH:mm"))}"
        }
        selectedRouteChanged(null)
    }

    fun selectedRouteChanged(newRoute: Int?) {
        selectedRoute = newRoute
        if (newRoute == null) {
            selectedRoutePoints.value = emptyList()
        } else {
            val route = routes?.elementAtOrNull(newRoute)
            selectedRoutePoints.value = route?.points?.map { routePoint ->
                routePoint.toRecyclerItem()
            } ?: emptyList()
            if (route == null) {
                selectedRoute = null
            }
        }
    }

    private fun RoutePoint.toRecyclerItem() =
        UniversalRecyclerItem(
            RoutePointDetails(point.name, if (visited) R.drawable.ic_check else R.drawable.ic_run),
            R.layout.route_point_details,
            BR.routePoint
        )

    fun showDatesError(value: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        showDatesError.value = value
    }
}

data class RoutePointDetails(val name: String, val image: Int)
