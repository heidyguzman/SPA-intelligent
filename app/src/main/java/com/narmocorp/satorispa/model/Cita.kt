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

    // Propiedades del CLIENTE (las que estaban causando el conflicto)
    val servicioNombre: String = "",
    val servicioImagen: String = "", // Mantenemos String para la vista del cliente

    // Propiedad del TERAPEUTA (que tu compañero ya usaba)
    val imagenServicio: String? = null, // Agregado para que la vista del Terapeuta compile

    val telefono: String = "",
    val terapeuta: String? = null,
    val updatedAt: Timestamp? = null,
    val comentarios: String? = null // Nuevo campo para comentarios
)