package com.narmocorp.satorispa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _passwordUpdateState = MutableStateFlow<PasswordUpdateState>(PasswordUpdateState.Idle)
    val passwordUpdateState: StateFlow<PasswordUpdateState> = _passwordUpdateState

    fun updateUserPassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _passwordUpdateState.value = PasswordUpdateState.Loading
            val user = FirebaseAuth.getInstance().currentUser

            if (user == null || user.email == null) {
                _passwordUpdateState.value = PasswordUpdateState.Error("Usuario no autenticado.")
                return@launch
            }

            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            _passwordUpdateState.value = PasswordUpdateState.Success
                        } else {
                            _passwordUpdateState.value = PasswordUpdateState.Error(updateTask.exception?.message ?: "Error al actualizar la contraseña.")
                        }
                    }
                } else {
                    _passwordUpdateState.value = PasswordUpdateState.Error(reauthTask.exception?.message ?: "La contraseña actual es incorrecta.")
                }
            }
        }
    }

    fun resetState() {
        _passwordUpdateState.value = PasswordUpdateState.Idle
    }
}

sealed class PasswordUpdateState {
    object Idle : PasswordUpdateState()
    object Loading : PasswordUpdateState()
    object Success : PasswordUpdateState()
    data class Error(val message: String) : PasswordUpdateState()
}