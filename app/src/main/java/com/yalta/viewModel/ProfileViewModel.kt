package com.yalta.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yalta.services.*
import common.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val authService: AuthService,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _role = MutableLiveData<String>()
    val role: LiveData<String> = _role

    private var _storedId = 0L

    val loggedOut = MutableLiveData(false)
    val changePassword = MutableLiveData(false)

    init {
        viewModelScope.launch(dispatcher) {
            val user = authService.getUser()
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
        val res = authService.logout()
        if (res) {
            loggedOut()
        }
    }

    private fun loggedOut() = viewModelScope.launch(Dispatchers.Main) {
        loggedOut.value = true
    }
}
