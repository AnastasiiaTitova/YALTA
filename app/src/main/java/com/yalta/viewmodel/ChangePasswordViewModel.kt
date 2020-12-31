package com.yalta.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yalta.repositories.PasswordRepo
import com.yalta.repositories.RealPasswordRepo
import com.yalta.services.PasswordService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(
    private val passwordService: PasswordService,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val firstPassword = MutableLiveData<String>()
    val secondPassword = MutableLiveData<String>()

    val closeActivity = MutableLiveData(false)
    val showPasswordError = MutableLiveData(false)
    val showConnectionError = MutableLiveData(false)

    fun changePassword()
    {
        showConnectionError.value = false
        val first = firstPassword.value
        val second = secondPassword.value
        if (!passwordsAreFine(first, second)) {
            showPasswordError(true)
            return
        }

        viewModelScope.launch(dispatcher) {
            val res = passwordService.changePassword(first!!)
            if (!res.isPresent) {
                showConnectionError()
            } else if (res.get()) {
                closeActivity()
            }
        }
    }

    private fun passwordsAreFine(first: String?, second: String?) : Boolean {
        return first != null && second != null && first == second
    }

    private fun closeActivity() = viewModelScope.launch(Dispatchers.Main) {
        closeActivity.value = true
    }

    private fun showConnectionError() = viewModelScope.launch(Dispatchers.Main) {
        showConnectionError.value = true
    }

    fun showPasswordError(value: Boolean) {
        showPasswordError.value = value
    }
}
