package com.yalta.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yalta.repositories.LogoutRepo
import com.yalta.repositories.RealLogoutRepo
import com.yalta.repositories.RealUserRepo
import com.yalta.repositories.UserRepo
import com.yalta.services.*
import common.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    userRepo: UserRepo = RealUserRepo(),
    logoutRepo: LogoutRepo = RealLogoutRepo(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _role = MutableLiveData<String>()
    val role: LiveData<String> = _role

    private val _userService = UserService(userRepo)
    private val _logoutService = LogoutService(logoutRepo)
    private var _storedId = 0L

    val loggedOut = MutableLiveData<Boolean>()
    val changePassword = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch(dispatcher) {
            val user = _userService.getUser()
            if (user != null) {
                _storedId = user.id!!
                updateUser(user)
            }
        }
    }

    fun changePassword() {
        changePassword.value = true
    }

    private fun updateUser(user: User) = viewModelScope.launch(Dispatchers.Main) {
        _userName.value = user.name
        _role.value = user.role.toString()
    }

    fun logout() = viewModelScope.launch(dispatcher) {
        val res = _logoutService.logout()
        if (res) {
            loggedOut()
        }
    }

    private fun loggedOut() = viewModelScope.launch(Dispatchers.Main) {
        loggedOut.value = true
    }
}
