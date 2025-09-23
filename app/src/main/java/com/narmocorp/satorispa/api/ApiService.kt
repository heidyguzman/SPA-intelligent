package com.narmocorp.satorispa.api

import com.narmocorp.satorispa.models.LoginRequest
import com.narmocorp.satorispa.models.Usuario
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.Body

interface ApiService {
    // GET /usuarios
    @GET("usuarios")
    fun getUsuarios(): Call<List<Usuario>>

    //mandar llamar el endpoint de login
    // GET /login
    @GET("login")
    fun login(@Query("correo") correo: String, @Query("contrasena") contrasena: String): Call<Usuario>

    // POST /usuarios
    @POST("usuarios")
    fun registerUser(@Body usuario: Usuario): Call<Usuario>
}




