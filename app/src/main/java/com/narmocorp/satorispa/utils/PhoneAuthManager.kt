// Archivo: com.narmocorp.satorispa.utils.PhoneAuthManager.kt
package com.narmocorp.satorispa.utils

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

/**
 * Interfaz para notificar a la UI sobre los cambios en el proceso de verificación.
 */
interface PhoneAuthListener {
    // Éxito en la verificación automática o en la vinculación manual
    fun onVerificationSuccess()

    // El código SMS fue enviado. Devuelve el ID.
    fun onCodeSent(verificationId: String)

    // Fallo en la verificación (red, seguridad, número incorrecto)
    fun onVerificationFailed(error: String)
}

/**
 * Clase que maneja el flujo de Firebase Phone Authentication.
 */
class PhoneAuthManager(
    private val activity: Activity
) {
    private val auth = FirebaseAuth.getInstance()
    private val phoneAuthProvider = PhoneAuthProvider.getInstance()

    /**
     * Inicia el proceso de envío del código SMS.
     * @param phoneNumber El número debe incluir el código de país, ej: "+521234567890".
     * @param listener El callback para notificar los resultados a la UI.
     */
    fun verifyPhoneNumber(phoneNumber: String, listener: PhoneAuthListener) {
        if (phoneNumber.length != 10) {
            listener.onVerificationFailed("El número debe tener 10 dígitos (sin el código de país).")
            return
        }

        // 🚨 IMPORTANTE: Se agrega el código de país aquí si la vista solo pasa 10 dígitos.
        // Asumiendo que el código de país es +52 (México). AJUSTAR SI ES NECESARIO.
        val fullNumber = "+52$phoneNumber"

        val verificationCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Verificación instantánea o éxito del código.
                signInWithCredential(credential, listener)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // Notifica a la UI el error
                listener.onVerificationFailed(e.localizedMessage ?: "Error desconocido en Firebase.")
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                // Notifica a la UI que muestre el campo de OTP
                listener.onCodeSent(verificationId)
            }
        }

        phoneAuthProvider.verifyPhoneNumber(
            fullNumber,
            60, // Timeout en segundos
            TimeUnit.SECONDS,
            activity,
            verificationCallbacks
        )
    }

    /**
     * Intenta autenticar al usuario con el código OTP y el verificationId.
     */
    fun signInWithOtp(verificationId: String, code: String, listener: PhoneAuthListener) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential, listener)
    }

    fun signInWithCredential(credential: PhoneAuthCredential, listener: PhoneAuthListener) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // VINCULA el teléfono a la cuenta existente.
            currentUser.linkWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        listener.onVerificationSuccess()
                    } else {
                        val error = task.exception?.localizedMessage ?: "Error al vincular el teléfono."
                        listener.onVerificationFailed(error)
                    }
                }
        } else {
            // Este caso es un fallback, si el usuario no está autenticado previamente
            auth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        listener.onVerificationSuccess()
                    } else {
                        val error = task.exception?.localizedMessage ?: "Error al iniciar sesión con OTP."
                        listener.onVerificationFailed(error)
                    }
                }
        }
    }
}