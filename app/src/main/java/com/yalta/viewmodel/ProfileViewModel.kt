package com.yalta.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yalta.services.*
import common.User

class ProfileViewModel(repo: UserRepo = HardcodedUserRepo()) : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _role = MutableLiveData<String>()
    val role: LiveData<String> = _role

    private val _userService = UserService(repo)
    private var _storedId = 0L

    init {
        val user = _userService.getUser()
        if (user != null) {
            _storedId = user.id!!
            updateUser(user)
        }
    }

    fun changeValue() {
        val user = _userService.changeUser(_storedId)
        if (user != null) {
            updateUser(user)
        }
    }

    private fun updateUser(user: User) {
        _userName.value = user.name
        _role.value = user.role.toString()
    }
}
