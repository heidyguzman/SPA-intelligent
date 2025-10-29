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

    /**
     * Carga o recarga los datos del usuario desde Firebase
     * Esta función puede ser llamada desde cualquier pantalla para refrescar los datos
     */
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
                        Log.d(TAG, "Datos cargados exitosamente: ${user.nombre} ${user.apellido}")
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

    /**
     * Recarga los datos sin mostrar el estado de Loading
     * Útil para actualizaciones en segundo plano
     */
    fun refreshUserDataSilently() {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                if (currentUser == null) return@launch

                val uid = currentUser.uid
                Log.d(TAG, "Refrescando datos silenciosamente: $uid")

                val document = firestore.collection("usuarios")
                    .document(uid)
                    .get(com.google.firebase.firestore.Source.SERVER)
                    .await()

                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        _userState.value = UserState.Success(user)
                        Log.d(TAG, "Datos refrescados: ${user.nombre} ${user.apellido}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al refrescar datos", e)
                // No cambiamos el estado en caso de error para mantener los datos actuales
            }
        }
    }
}