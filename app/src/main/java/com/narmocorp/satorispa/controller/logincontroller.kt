package com.narmocorp.satorispa.controller

import android.content.Context
import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.narmocorp.satorispa.utils.SessionManager

private const val TAG = "LoginController"

fun loginUser(
    email: String,
    password: String,
    keepSession: Boolean,
    context: Context,
    navController: NavController,
    onLoginError: (String) -> Unit
) {
    if (email.isBlank() || password.isBlank()) {
        onLoginError("El correo y la contraseña no pueden estar vacíos.")
        return
    }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val sessionManager = SessionManager(context)

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sessionManager.saveSessionPreference(keepSession)
                val user = auth.currentUser
                Log.d(TAG, "signInWithEmailAndPassword successful. currentUser=$user")
                if (user != null) {
                    val uid = user.uid
                    Log.d(TAG, "Authenticated user uid=$uid")
                    db.collection("usuarios").document(uid)
                        .get()
                        .addOnSuccessListener { document ->
                            Log.d(TAG, "Firestore get() success for uid=$uid; exists=${document?.exists()}")
                            if (document != null && document.exists()) {
                                val dataMap = document.data
                                Log.d(TAG, "Documento data=$dataMap")
                                val rolRaw = document.getString("rol")
                                Log.d(TAG, "rol raw='$rolRaw'")
                                val rol = rolRaw?.trim()?.lowercase()
                                if (rol == "cliente") {
                                    navController.navigate("cliente_home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else if (rol == "terapeuta") {
                                    navController.navigate("terapeuta_home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    val dataStr = document.data?.toString() ?: "(sin datos)"
                                    val msg = "Rol de usuario no reconocido. Valor guardado: '$rolRaw'. Documento: $dataStr"
                                    Log.d(TAG, msg)
                                    onLoginError(msg)
                                }
                            } else {
                                val msg = "No se encontró el usuario en la base de datos. uid=$uid"
                                Log.d(TAG, msg)
                                onLoginError(msg)
                            }
                        }
                        .addOnFailureListener { e ->
                            val msg = "Error al obtener datos del usuario: ${e.message}"
                            Log.e(TAG, msg, e)
                            onLoginError(msg)
                        }
                } else {
                    val msg = "Usuario autenticado pero currentUser es null."
                    Log.d(TAG, msg)
                    onLoginError(msg)
                }
            } else {
                val msg = "Error de autenticación: ${task.exception?.message}"
                Log.d(TAG, msg, task.exception)
                onLoginError(msg)
            }
        }
        .addOnFailureListener { e ->
            val msg = "Error al conectar con el servicio de autenticación: ${e.message}"
            Log.e(TAG, msg, e)
            onLoginError(msg)
        }
}
