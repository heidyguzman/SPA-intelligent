package com.narmocorp.satorispa.model

import com.google.firebase.Timestamp

data class User(
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val rol: String = "",
    val imagenUrl: String = "", // URL de la imagen de perfil
    val nfc: String = "",
    val createdAt: Timestamp? = null
)