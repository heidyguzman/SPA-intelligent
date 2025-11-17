package com.narmocorp.satorispa.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration // Importación necesaria
import com.narmocorp.satorispa.model.User // Asumo que tienes esta clase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val TAG = "ClientHomeViewModel"

class ClientHomeViewModel(application: Application) : AndroidViewModel(application) {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val sharedPreferences = application.getSharedPreferences("NfcPref", Context.MODE_PRIVATE)

    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    // Variable para mantener el listener abierto
    private var userListenerRegistration: ListenerRegistration? = null

    init {
        // 1. Empezar a escuchar los datos al inicializar el ViewModel
        startUserListener()
    }

    /**
     * Se suscribe a los datos del usuario en tiempo real.
     * Cada vez que se cambia el nombre o apellido en Firestore, el userState se actualiza automáticamente.
     */
    private fun startUserListener() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _userState.value = UserState.Error("Usuario no autenticado")
            return
        }

        val uid = currentUser.uid
        Log.d(TAG, "Iniciando listener para datos del usuario: $uid")

        // 2. Usar addSnapshotListener (Lectura en Tiempo Real)
        userListenerRegistration = firestore.collection("usuarios")
            .document(uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e(TAG, "Error al escuchar datos del usuario", e)
                    _userState.value = UserState.Error(e.message ?: "Error desconocido")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(User::class.java)
                    if (user != null) {
                        _userState.value = UserState.Success(user)
                        // Guardar el NFC UID en SharedPreferences
                        with(sharedPreferences.edit()) {
                            putString("nfc_uid", user.nfc)
                            apply()
                        }
                    } else {
                        _userState.value = UserState.Error("Error al parsear datos del usuario")
                    }
                } else {
                    _userState.value = UserState.Error("Usuario no encontrado en la base de datos")
                }
            }
    }

    /**
     * 3. Detener el listener cuando el ViewModel se destruye para evitar fugas de memoria.
     */
    override fun onCleared() {
        super.onCleared()
        userListenerRegistration?.remove()
        Log.d(TAG, "Listener de datos de usuario detenido.")
    }

    // 🔑 ELIMINADO: Ya no se necesitan loadUserData() ni refreshUserDataSilently()
}
