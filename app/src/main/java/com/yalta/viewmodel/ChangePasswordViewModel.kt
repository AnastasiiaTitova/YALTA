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

class ChangePasswordViewModel(
    repo: PasswordRepo = RealPasswordRepo(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _passwordService = PasswordService(repo)

    val firstPassword = MutableLiveData<String>()
    val secondPassword = MutableLiveData<String>()

    val closeActivity = MutableLiveData(false)
    val showError = MutableLiveData(false)

    fun changePassword()
    {
        val first = firstPassword.value
        val second = secondPassword.value
        if (!passwordsAreFine(first, second)) {
            showError.value = true
            return
        }

        viewModelScope.launch(dispatcher) {
            val res = _passwordService.changePassword(first!!)
            if (res) {
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
}
