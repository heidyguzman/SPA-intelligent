package com.narmocorp.satorispa.controller

import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun loginUser(
    email: String,
    password: String,
    navController: NavController,
    onLoginError: (String) -> Unit
) {
    if (email.isBlank() || password.isBlank()) {
        onLoginError("El correo y la contraseña no pueden estar vacíos.")
        return
    }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    db.collection("usuarios").document(user.uid)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                val rol = document.getString("rol")
                                if (rol == "cliente") {
                                    navController.navigate("cliente_home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else if (rol == "terapeuta") {
                                    navController.navigate("terapeuta_home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    onLoginError("Rol de usuario no reconocido.")
                                }
                            } else {
                                onLoginError("No se encontró el usuario en la base de datos.")
                            }
                        }
                        .addOnFailureListener { e ->
                            onLoginError("Error al obtener datos del usuario: ${e.message}")
                        }
                }
            } else {
                onLoginError("Error de autenticación: ${task.exception?.message}")
            }
        }
}