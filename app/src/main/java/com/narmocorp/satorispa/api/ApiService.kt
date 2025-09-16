package com.narmocorp.satorispa.api

import com.narmocorp.satorispa.models.Usuario
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query // Importa Query

interface ApiService {
    // GET /usuarios
    @GET("usuarios")
    fun getUsuarios(): Call<List<Usuario>>

    // GET /login
    @GET("login")
    fun loginUsuario(@Query("correo") correo: String, @Query("contrasena") contrasena: String): Call<Usuario>
}