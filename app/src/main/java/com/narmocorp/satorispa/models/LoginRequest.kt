package com.narmocorp.satorispa.models

data class LoginRequest(
    val correo: String,
    val contrasena: String
)