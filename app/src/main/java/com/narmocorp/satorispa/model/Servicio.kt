package com.narmocorp.satorispa.model

import com.google.firebase.firestore.PropertyName

data class Servicio(
    val id: String = "",
    val categoria: String = "",
    val createdAt: String = "",
    val descripcion: String = "",
    val estado: String = "",
    val imagen: String = "",
    val precio: Double = 0.0,
    @get:PropertyName("servicio") @set:PropertyName("servicio") var nombre: String = ""
)
