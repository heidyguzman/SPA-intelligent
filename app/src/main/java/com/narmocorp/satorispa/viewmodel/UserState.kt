package com.narmocorp.satorispa.viewmodel

import com.narmocorp.satorispa.model.User

sealed class UserState {
    object Loading : UserState()
    data class Success(val user: User) : UserState()
    data class Error(val message: String) : UserState()
}