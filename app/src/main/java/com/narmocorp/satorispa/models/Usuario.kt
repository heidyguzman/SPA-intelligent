package com.narmocorp.satorispa.models

data class Usuario(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val contrasena: String
)