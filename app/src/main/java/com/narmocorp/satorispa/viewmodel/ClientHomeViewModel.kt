package com.narmocorp.satorispa.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.narmocorp.satorispa.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val TAG = "ClientHomeViewModel"

class ClientHomeViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    init {
        loadUserData()
    }

    fun loadUserData() {
        viewModelScope.launch {
            try {
                _userState.value = UserState.Loading

                val currentUser = auth.currentUser
                if (currentUser == null) {
                    _userState.value = UserState.Error("Usuario no autenticado")
                    return@launch
                }

                val uid = currentUser.uid
                Log.d(TAG, "Cargando datos del usuario: $uid")

                val document = firestore.collection("usuarios")
                    .document(uid)
                    .get()
                    .await()

                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        _userState.value = UserState.Success(user)
                        Log.d(TAG, "Datos cargados: ${user.nombre} ${user.apellido}")
                    } else {
                        _userState.value = UserState.Error("Error al parsear datos del usuario")
                    }
                } else {
                    _userState.value = UserState.Error("Usuario no encontrado en la base de datos")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar datos del usuario", e)
                _userState.value = UserState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}

sealed class UserState {
    object Loading : UserState()
    data class Success(val user: User) : UserState()
    data class Error(val message: String) : UserState()
}