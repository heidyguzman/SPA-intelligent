package com.narmocorp.satorispa.model

import com.google.firebase.Timestamp

// Modelo que representa un documento de la colección "citas" en Firestore
data class Cita(
    val id: String = "",
    val cliente: String = "",
    val cliente_id: String? = null,
    val createdAt: Timestamp? = null,
    val estado: String = "",
    val fecha: String = "",
    val hora: String = "",
    val servicio: String = "",
    val telefono: String = "",
    val terapeuta: String? = null,
    val updatedAt: Timestamp? = null,
    val imagenServicio: String? = null // URL de la imagen del servicio (opcional)
)
