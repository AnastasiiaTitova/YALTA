package com.yalta.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yalta.repositories.RealUserRepo
import com.yalta.repositories.UserRepo
import com.yalta.services.*
import common.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    repo: UserRepo = RealUserRepo(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _role = MutableLiveData<String>()
    val role: LiveData<String> = _role

    private val _userService = UserService(repo)
    private var _storedId = 0L

    init {
        viewModelScope.launch(dispatcher) {
            val user = _userService.getUser()
            if (user != null) {
                _storedId = user.id!!
                updateUser(user)
            }
        }
    }

    fun changeValue() = viewModelScope.launch(dispatcher) {
        val user = _userService.changeUser(_storedId)
        if (user != null) {
            updateUser(user)
        }
    }

    private fun updateUser(user: User) = viewModelScope.launch(Dispatchers.Main) {
        _userName.value = user.name
        _role.value = user.role.toString()
    }
}
