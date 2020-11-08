package com.yalta.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import common.Admin
import common.Driver
import common.Role
import common.User

class ProfileViewModel : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _role = MutableLiveData<String>()
    val role: LiveData<String> = _role

    private var user = User(1, "root", "root", Driver)

    init {
        _userName.value = user.name
        val role : Role = user.role!!
        _role.value = when (role) {
            is Driver -> "driver"
            is Admin -> "admin"
        }
    }

    fun changeValue() {
        _role.value = "changed"
    }
}