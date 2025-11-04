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
                val user = auth.currentUser
                if (user != null && user.isEmailVerified) {
                    // Email is verified, proceed to get user role from Firestore
                    sessionManager.saveSessionPreference(keepSession)
                    Log.d(TAG, "Authenticated and verified user uid=${user.uid}")

                    db.collection("usuarios").document(user.uid)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                val rol = document.getString("rol")?.trim()?.lowercase()
                                Log.d(TAG, "User rol is '$rol'")
                                when (rol) {
                                    "cliente" -> navController.navigate("cliente_home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                    "terapeuta" -> navController.navigate("terapeuta_home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                    else -> {
                                        val rolRaw = document.getString("rol")
                                        val msg = "Rol de usuario no reconocido: '$rolRaw'"
                                        Log.d(TAG, msg)
                                        onLoginError(msg)
                                    }
                                }
                            } else {
                                val msg = "No se encontró el usuario en la base de datos. uid=${user.uid}"
                                Log.d(TAG, msg)
                                auth.signOut() // Sign out if user data is missing
                                onLoginError(msg)
                            }
                        }
                        .addOnFailureListener { e ->
                            val msg = "Error al obtener datos del usuario: ${e.message}"
                            Log.e(TAG, msg, e)
                            auth.signOut()
                            onLoginError(msg)
                        }
                } else if (user != null && !user.isEmailVerified) {
                    // User is not verified
                    Log.d(TAG, "Login failed: email not verified for user ${user.email}")
                    auth.signOut() // Sign out to prevent unverified access
                    onLoginError("Por favor, verifica tu correo electrónico para poder iniciar sesión.")
                } else if (user == null) {
                    // This case should be rare but is good to handle
                    val msg = "Usuario autenticado pero currentUser es null."
                    Log.d(TAG, msg)
                    onLoginError(msg)
                }

            } else {
                // Authentication task failed
                val msg = task.exception?.message ?: "Correo o contraseña incorrectos."
                Log.d(TAG, "Authentication failed: $msg", task.exception)
                onLoginError(msg)
            }
        }
        .addOnFailureListener { e ->
            // This listener is for network errors or other issues before the task completes.
            val msg = "Error de conexión: ${e.message}"
            Log.e(TAG, msg, e)
            onLoginError(msg)
        }
}
