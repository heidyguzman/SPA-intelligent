package com.narmocorp.satorispa.model

import com.google.firebase.firestore.PropertyName

data class Servicio(
    val id: String = "",
    @get:PropertyName("categoriaId") @set:PropertyName("categoriaId") var categoriaId: String = "",
    @get:PropertyName("categoriaNombre") @set:PropertyName("categoriaNombre") var categoriaNombre: String = "",
    val descripcion: String = "",
    val duracion: String = "",
    val estado: String = "",
    val imagen: String = "",
    val precio: String = "0",
    val servicio: String = "",
)
