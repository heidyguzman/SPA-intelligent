package com.narmocorp.satorispa.controller

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

object RegistroController {
    /**
     * Registers a new user in Firebase Authentication, sends a verification email,
     * and creates a Firestore document inside the `usuarios` collection with rol = "cliente".
     * The user is signed out after registration and must verify their email before logging in.
     *
     * onResult will be invoked with (success, message).
     */
    fun registerUser(
        nombre: String,
        apellido: String,
        correo: String,
        contrasena: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    val user = auth.currentUser
                    if (user == null) {
                        onResult(false, "Error: UID no disponible después de crear la cuenta")
                        return@addOnCompleteListener
                    }

                    user.sendEmailVerification()
                        .addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                val userData = hashMapOf(
                                    "nombre" to nombre,
                                    "apellido" to apellido,
                                    "correo" to correo,
                                    "rol" to "cliente",
                                    "createdAt" to Timestamp.now()
                                )

                                firestore.collection("usuarios").document(user.uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        auth.signOut()
                                        onResult(true, "Registro exitoso. Se ha enviado un correo de verificación.")
                                    }
                                    .addOnFailureListener { e ->
                                        // If Firestore write fails, delete the created Auth user to avoid orphaned accounts.
                                        user.delete()
                                        onResult(false, e.message ?: "Error al guardar datos del usuario")
                                    }
                            } else {
                                // Failed to send verification email, delete the user
                                user.delete()
                                onResult(false, verificationTask.exception?.message ?: "Error al enviar correo de verificación")
                            }
                        }
                } else {
                    val message = authTask.exception?.message ?: "Error al crear cuenta"
                    onResult(false, message)
                }
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Error en la conexión")
            }
    }
}
