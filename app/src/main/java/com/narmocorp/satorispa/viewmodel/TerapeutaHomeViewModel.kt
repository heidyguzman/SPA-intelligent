package com.narmocorp.satorispa.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.narmocorp.satorispa.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class TerapeutaHomeViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState

    init {
        loadUserData()
    }

    fun loadUserData() {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            fetchUserData()
        }
    }

    fun refreshUserDataSilently() {
        viewModelScope.launch {
            fetchUserData()
        }
    }

    private suspend fun fetchUserData() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _userState.value = UserState.Error("No se ha iniciado sesión.")
            return
        }

        try {
            val document = db.collection("usuarios").document(userId).get().await()
            if (document.exists()) {
                // Deserialización manual para manejar inconsistencias en los datos
                val nombre = document.getString("nombre") ?: ""
                val apellido = document.getString("apellido") ?: ""
                val correo = document.getString("correo") ?: ""
                val rol = document.getString("rol") ?: ""
                val imagenUrl = document.getString("imagenUrl") ?: ""

                val createdAt = when (val ts = document.get("createdAt")) {
                    is Timestamp -> ts
                    is String -> {
                        // Intenta convertir el texto a Timestamp
                        try {
                            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                            format.timeZone = TimeZone.getTimeZone("UTC")
                            val date = format.parse(ts)
                            if (date != null) Timestamp(date) else Timestamp.now()
                        } catch (e: Exception) {
                            Timestamp.now() // Si falla, usa la fecha actual
                        }
                    }
                    else -> Timestamp.now() // Si no es ni fecha ni texto, usa la fecha actual
                }

                val user = User(
                    nombre = nombre,
                    apellido = apellido,
                    correo = correo,
                    rol = rol,
                    imagenUrl = imagenUrl,
                    createdAt = createdAt
                )
                _userState.value = UserState.Success(user)

            } else {
                _userState.value = UserState.Error("No se encontraron datos para este usuario.")
            }
        } catch (e: Exception) {
            Log.e("TerapeutaHomeViewModel", "Error loading user data", e)
            _userState.value = UserState.Error("Error al cargar los datos: ${e.message}")
        }
    }
}
