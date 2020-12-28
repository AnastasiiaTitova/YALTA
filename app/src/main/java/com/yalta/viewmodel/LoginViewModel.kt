package com.yalta.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yalta.services.LoginService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val service: LoginService,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val showLoginError = MutableLiveData(false)
    val showConnectionError = MutableLiveData(false)
    val user = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val loggedIn = MutableLiveData(false)

    fun login() {
        showConnectionError.value = false
        viewModelScope.launch(dispatcher) {
            val loggedIn = service.login(user.value!!, password.value!!)
            when {
                !loggedIn.isPresent -> {
                    showConnectionError()
                }
                loggedIn.get() -> {
                    successfulLogin()
                }
                else -> {
                    showLoginError(true)
                }
            }
        }
    }

    private fun showConnectionError() = viewModelScope.launch(Dispatchers.Main) {
        showConnectionError.value = true
    }

    private fun successfulLogin() = viewModelScope.launch(Dispatchers.Main) {
        loggedIn.value = true
    }

    fun showLoginError(value: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        showLoginError.value = value
    }
}
