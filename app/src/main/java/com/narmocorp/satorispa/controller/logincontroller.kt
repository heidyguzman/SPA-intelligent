package com.narmocorp.satorispa.controller

import android.content.Context
import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.narmocorp.satorispa.utils.NotificacionesManager
import com.narmocorp.satorispa.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

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
    val notificacionesManager = NotificacionesManager()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null && user.isEmailVerified) {
                    sessionManager.saveSessionPreference(keepSession)
                    Log.d(TAG, "Authenticated and verified user uid=${user.uid}")

                    val userDocRef = db.collection("usuarios").document(user.uid)

                    userDocRef.get()
                        .addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                // Check for NFC UID
                                if (!document.contains("nfc")) {
                                    val nfcUid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).uppercase()
                                    userDocRef.update("nfc", nfcUid)
                                        .addOnSuccessListener {
                                            Log.d(TAG, "NFC UID generated and saved for user ${user.uid}")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e(TAG, "Error saving NFC UID for user ${user.uid}", e)
                                        }
                                }

                                val notificacionBienvenidaEnviada = document.getBoolean("notificacionBienvenidaEnviada") ?: false

                                // Get the latest FCM token and update it in Firestore for the current user.
                                FirebaseMessaging.getInstance().token.addOnCompleteListener { tokenTask ->
                                    if (tokenTask.isSuccessful) {
                                        val token = tokenTask.result

                                        if (!notificacionBienvenidaEnviada) {
                                            // First login: update token, set welcome flag, and send notification.
                                            userDocRef.update("fcmToken", token, "notificacionBienvenidaEnviada", true)
                                                .addOnSuccessListener {
                                                    Log.d(TAG, "FCM token and welcome flag saved for user ${user.uid}")
                                                    // Send welcome notification in a background thread.
                                                    CoroutineScope(Dispatchers.IO).launch {
                                                        notificacionesManager.enviarBienvenida(user.uid)
                                                    }
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e(TAG, "Error saving FCM token and/or welcome flag for user ${user.uid}", e)
                                                }
                                        } else {
                                            // Subsequent logins: just update the FCM token.
                                            userDocRef.update("fcmToken", token)
                                                .addOnSuccessListener {
                                                    Log.d(TAG, "FCM token updated for user ${user.uid}")
                                                }
                                                .addOnFailureListener { e ->
                                                     Log.e(TAG, "Error updating FCM token for user ${user.uid}", e)
                                                }
                                        }
                                    } else {
                                        Log.w(TAG, "Fetching FCM registration token failed", tokenTask.exception)
                                    }
                                }

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
                                auth.signOut()
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
                    Log.d(TAG, "Login failed: email not verified for user ${user.email}")
                    auth.signOut()
                    onLoginError("Por favor, verifica tu correo electrónico para poder iniciar sesión.")
                } else if (user == null) {
                    val msg = "Usuario autenticado pero currentUser es null."
                    Log.d(TAG, msg)
                    onLoginError(msg)
                }

            } else {
                val msg = task.exception?.message ?: "Correo o contraseña incorrectos."
                Log.d(TAG, "Authentication failed: $msg", task.exception)
                onLoginError(msg)
            }
        }
        .addOnFailureListener { e ->
            val msg = "Error de conexión: ${e.message}"
            Log.e(TAG, msg, e)
            onLoginError(msg)
        }
}
