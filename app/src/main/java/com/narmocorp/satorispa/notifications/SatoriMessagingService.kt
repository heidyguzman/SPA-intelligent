package com.narmocorp.satorispa.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log

class SatoriMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM", "Notificación recibida de: ${remoteMessage.from}")
        Log.d("FCM", "Data payload: ${remoteMessage.data}")

        // Extraer datos de la notificación
        val title = remoteMessage.data["title"] ?: remoteMessage.notification?.title ?: "Satori SPA"
        val body = remoteMessage.data["body"] ?: remoteMessage.notification?.body ?: ""
        val tipo = remoteMessage.data["tipo"] ?: "general"
        val usuarioId = remoteMessage.data["usuarioId"] // ID del destinatario

        // Mostrar la notificación
        mostrarNotificacion(title, body, tipo, usuarioId)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Token nuevo: $token")

        // Guardar el token en Firestore
        guardarTokenEnFirebase(token)
    }

    private fun mostrarNotificacion(titulo: String, mensaje: String, tipo: String, usuarioId: String?) {
        val canalId = "satori_notificaciones"
        val notificationId = System.currentTimeMillis().toInt()

        // Crear canal de notificaciones (requerido para Android 8+)
        crearCanalNotificacion(canalId)

        // Intent para abrir la app
        val intent = Intent(this, com.narmocorp.satorispa.MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("tipo_notificacion", tipo)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construir la notificación
        val builder = NotificationCompat.Builder(this, canalId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Cambiar por tu ícono
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())

        // Guardar la notificación en Firestore
        guardarNotificacionEnFirestore(titulo, mensaje, tipo, usuarioId)
    }

    private fun guardarNotificacionEnFirestore(titulo: String, mensaje: String, tipo: String, usuarioIdFromMessage: String?) {
        // Priorizar el ID del mensaje; si no existe, usar el del usuario actual (si está logueado)
        val targetUsuarioId = usuarioIdFromMessage ?: com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid

        if (targetUsuarioId != null) {
            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
            val notificacion = mapOf(
                "titulo" to titulo,
                "mensaje" to mensaje,
                "tipo" to tipo,
                "fecha" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "leida" to false
            )

            db.collection("usuarios").document(targetUsuarioId)
                .collection("notificaciones")
                .add(notificacion)
                .addOnSuccessListener {
                    Log.d("FCM", "Notificación guardada en Firestore para el usuario: $targetUsuarioId")
                }
                .addOnFailureListener { e ->
                    Log.e("FCM", "Error al guardar notificación", e)
                }
        }
    }

    private fun crearCanalNotificacion(canalId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                canalId,
                "Notificaciones Satori",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones de bienvenida, confirmaciones y recordatorios"
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canal)
        }
    }

    private fun guardarTokenEnFirebase(token: String) {
        val usuarioId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
        if (usuarioId != null) {
            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
            db.collection("usuarios").document(usuarioId).update(
                mapOf("fcmToken" to token)
            ).addOnSuccessListener {
                Log.d("FCM", "Token guardado en Firebase")
            }.addOnFailureListener { e ->
                Log.e("FCM", "Error al guardar token", e)
            }
        }
    }
}
