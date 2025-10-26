package com.narmocorp.satorispa.controller

import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuthUserCollisionException

private const val TAG = "AuthController"

object AuthController {

    /**
     * Reautentica al usuario con la contraseña actual y luego cambia la contraseña.
     * @param contrasenaActual Contraseña actual del usuario.
     * @param contrasenaNueva Nueva contraseña deseada.
     * @param onSuccess Callback en caso de éxito.
     * @param onError Callback en caso de error (con mensaje).
     */
    fun cambiarContrasena(
        contrasenaActual: String,
        contrasenaNueva: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null || user.email == null) {
            onError("No hay usuario autenticado o el correo no está disponible.")
            return
        }

        // 1. Crear credencial con el correo y la contraseña actual
        val credential = EmailAuthProvider.getCredential(user.email!!, contrasenaActual)

        // 2. Reautenticar al usuario para validar la contraseña actual
        user.reauthenticate(credential)
            .addOnSuccessListener {
                Log.d(TAG, "Reautenticación exitosa. Procediendo a cambiar contraseña.")

                // 3. Si la reautenticación es exitosa, cambiar la contraseña nueva
                user.updatePassword(contrasenaNueva)
                    .addOnSuccessListener {
                        Log.d(TAG, "Contraseña cambiada exitosamente.")
                        // PASO DE SEGURIDAD CRÍTICO: CERRAR LA SESIÓN INMEDIATAMENTE
                        auth.signOut()
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error al cambiar contraseña", e)
                        onError("Error al cambiar la contraseña: ${e.localizedMessage}")
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Reautenticación fallida", e)
                // Mensaje genérico por seguridad (para evitar revelar si el email es correcto o no)
                onError("Contraseña actual incorrecta. Por favor, verifica.")
            }
    }

    /**
     * Reautentica al usuario usando el token de sesión actual para evitar problemas de permisos
     * y luego cambia el correo y actualiza Firestore.
     * La contraseña actual se valida en la UI (EditarPerfilScreen) pero no se usa aquí.
     */
    fun cambiarCorreo(
        nuevoCorreo: String,
        contrasenaActual: String, // 🔑 VUELVE A SER NECESARIO ESTE ARGUMENTO
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val firestore = FirebaseFirestore.getInstance()

        if (user == null || user.email == null || user.uid == null) {
            onError("No hay usuario autenticado.")
            return
        }

        // 1. Crear credencial con el correo y la CONTRASEÑA ACTUAL
        // Esta es la única forma de reautenticación que ha funcionado en tu entorno.
        val credential = EmailAuthProvider.getCredential(user.email!!, contrasenaActual)

        // 2. Reautenticar al usuario para validar la contraseña actual
        user.reauthenticate(credential)
            .addOnSuccessListener {
                Log.d(TAG, "Reautenticación con contraseña exitosa. Procediendo a cambiar el correo.")

                // 3. ACTUALIZAR CORREO en Firebase Auth
                user.updateEmail(nuevoCorreo)
                    .addOnSuccessListener {
                        Log.d(TAG, "Correo actualizado en Auth exitosamente. Procediendo a Firestore.")

                        // 4. ACTUALIZAR CORREO en Firestore
                        firestore.collection("usuarios").document(user.uid).update("correo", nuevoCorreo)
                            .addOnSuccessListener {
                                Log.d(TAG, "Correo actualizado en Firestore exitosamente.")
                                user.reload().addOnCompleteListener {
                                    onSuccess("¡Correo cambiado exitosamente!")
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error al actualizar correo en Firestore", e)
                                onError("Correo cambiado en sesión, pero error al actualizar en la base de datos.")
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error al cambiar correo en Auth", e)
                        val errorMessage = when (e) {
                            is FirebaseAuthUserCollisionException -> "Ese correo ya está registrado por otra cuenta."
                            else -> "Error al cambiar el correo: ${e.localizedMessage}"
                        }
                        onError(errorMessage)
                    }
            }
            .addOnFailureListener { e ->
                // Mensaje genérico de error si la contraseña actual es incorrecta
                Log.e(TAG, "Fallo al reautenticar con contraseña.", e)
                onError("Contraseña actual incorrecta. Por favor, verifica.")
            }
    }

    fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        Log.d("AuthController", "Sesión de usuario cerrada.")
    }
}