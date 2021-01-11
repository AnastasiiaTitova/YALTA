package com.yalta.viewModel

import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yalta.R
import com.yalta.services.RoutesService
import com.yalta.services.Storage
import com.yalta.utils.UniversalRecyclerItem
import common.Route
import common.RoutePoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*
import javax.inject.Inject

class DriverRoutesViewModel @Inject constructor(
    private val routesService: RoutesService,
    val storage: Storage,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    var selectedRoute: Int? = null
    val selectedRoutePoints = MutableLiveData<List<UniversalRecyclerItem>>()
    val fromDate = MutableLiveData<DateTime>()
    val toDate = MutableLiveData<DateTime>()
    val isRouteCurrent = MutableLiveData(false)

    private val timezone = DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Moscow"))

    init {
        storage.routes.observeForever { routes ->
            if (routes.isEmpty()) {
                selectedRouteChanged(selectedRoute)
            }
        }
        getSelectedRoutes(
            DateTime.now(timezone).withHourOfDay(0).withMinuteOfHour(0),
            DateTime.now(timezone).plusDays(7).withHourOfDay(23).withMinuteOfHour(59)
        )
    }

    fun getSomeRoutes() {
        var from = fromDate.value
        var to = toDate.value

        from = from ?: DateTime.now(timezone)
        from = from!!.withHourOfDay(0).withMinuteOfHour(0)
        to = to ?: DateTime.now(timezone)
        to = to!!.withHourOfDay(23).withMinuteOfHour(59)

        getSelectedRoutes(from!!, to!!)
    }

    private fun getSelectedRoutes(from: DateTime, to: DateTime) = viewModelScope.launch(dispatcher) {
        val result = routesService.getRoutes(from.toString(), to.toString())
        if (result.isPresent) {
            setRoutes(result.get().reversed())
        }
    }

    private fun setRoutes(newRoutes: List<Route>) = viewModelScope.launch(Dispatchers.Main) {
        storage.routes.value = newRoutes
    }

    fun selectedRouteChanged(newRoute: Int?) {
        selectedRoute = newRoute
        if (newRoute == null) {
            selectedRoutePoints.value = emptyList()
            isRouteCurrent.value = false
        } else {
            val route = storage.routes.value?.elementAtOrNull(newRoute)

            isRouteCurrent.value = isRouteDateNow(route)

            selectedRoutePoints.value = route?.points?.map { routePoint ->
                routePoint.toRecyclerItem()
            } ?: emptyList()
            if (route == null) {
                selectedRoute = null
            }
        }
    }

    fun isRouteDateNow(route: Route?): Boolean =
        route?.routeDate?.withTimeAtStartOfDay()
            ?.isEqual(DateTime.now().withTimeAtStartOfDay())
            ?: false

    private fun RoutePoint.toRecyclerItem() =
        UniversalRecyclerItem(
            RoutePointDetails(point.name, if (visited) R.drawable.ic_check else R.drawable.ic_run),
            R.layout.route_point_details,
            BR.routePoint
        )

}

data class RoutePointDetails(val name: String, val image: Int)
