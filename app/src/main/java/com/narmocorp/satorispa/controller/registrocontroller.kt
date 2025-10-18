package com.narmocorp.satorispa.controller

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

object RegistroController {
    /**
     * Registers a new user in Firebase Authentication (email/password) and creates a
     * Firestore document inside the `usuarios` collection with rol = "cliente".
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
                    val uid = user?.uid
                    if (uid == null) {
                        onResult(false, "Error: UID no disponible después de crear la cuenta")
                        return@addOnCompleteListener
                    }

                    val userData = hashMapOf(
                        "nombre" to nombre,
                        "apellido" to apellido,
                        "correo" to correo,
                        "rol" to "cliente",
                        "createdAt" to Timestamp.now()
                    )

                    firestore.collection("usuarios").document(uid)
                        .set(userData)
                        .addOnSuccessListener {
                            // Sign out so the app is not left authenticated after registration.
                            auth.signOut()
                            onResult(true, "Registro exitoso")
                        }
                        .addOnFailureListener { e ->
                            // If Firestore write fails, delete the created Auth user to avoid orphaned accounts.
                            auth.currentUser?.delete()
                            onResult(false, e.message ?: "Error al guardar datos del usuario")
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
