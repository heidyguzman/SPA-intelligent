package com.narmocorp.satorispa.utils

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await

class NotificacionesManager {

    private val db = FirebaseFirestore.getInstance()
    private val functions = FirebaseFunctions.getInstance()

    // Enviar notificación de BIENVENIDA
    suspend fun enviarBienvenida(usuarioId: String) {
        try {
            val data = hashMapOf(
                "usuarioId" to usuarioId,
                "tipo" to "bienvenida",
                "titulo" to "¡Bienvenido a Satori SPA!",
                "mensaje" to "Nos alegra tenerte aquí. Descubre nuestros servicios"
            )
            val result = functions.getHttpsCallable("enviarNotificacion").call(data).await()
            Log.d("NotificacionesManager", "Bienvenida enviada: $result")
        } catch (e: Exception) {
            Log.e("NotificacionesManager", "Error al enviar bienvenida", e)
        }
    }

    // Enviar notificación de CONFIRMACIÓN DE SERVICIO
    suspend fun enviarConfirmacionServicio(usuarioId: String, nombreServicio: String) {
        try {
            val data = hashMapOf(
                "usuarioId" to usuarioId,
                "tipo" to "confirmacion",
                "titulo" to "Servicio Confirmado ✓",
                "mensaje" to "Tu cita para $nombreServicio ha sido confirmada"
            )
            functions.getHttpsCallable("enviarNotificacion").call(data).await()
            Log.d("Notificaciones", "Confirmación enviada")
        } catch (e: Exception) {
            Log.e("Notificaciones", "Error al enviar confirmación", e)
        }
    }

    // Enviar notificación de RECORDATORIO (1 día antes)
    suspend fun enviarRecordatorio(usuarioId: String, nombreServicio: String, fecha: String) {
        try {
            val data = hashMapOf(
                "usuarioId" to usuarioId,
                "tipo" to "recordatorio",
                "titulo" to "Recordatorio de Cita",
                "mensaje" to "Tu cita para $nombreServicio es mañana a las $fecha"
            )
            functions.getHttpsCallable("enviarNotificacion").call(data).await()
            Log.d("Notificaciones", "Recordatorio enviado")
        } catch (e: Exception) {
            Log.e("Notificaciones", "Error al enviar recordatorio", e)
        }
    }
}