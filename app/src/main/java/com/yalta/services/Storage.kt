package com.yalta.services

import androidx.lifecycle.MutableLiveData
import common.Route
import javax.inject.Inject
import javax.inject.Singleton

interface Storage {
    val routes: MutableLiveData<List<Route>>
    fun update(updatedRoute: Route)
    fun clearData()
}

@Singleton
class StorageImpl @Inject constructor() : Storage {
    override val routes = MutableLiveData<List<Route>>(emptyList())

    override fun update(updatedRoute: Route) {
        if (routes.value != null) {
            val copy = routes.value?.toMutableList()
            val item = copy?.find { route -> route.id == updatedRoute.id }
            if (item != null) {
                copy[copy.indexOf(item)] = updatedRoute
                routes.value = copy
            }
        }
    }

    override fun clearData() {
        routes.postValue(emptyList())
    }
}
