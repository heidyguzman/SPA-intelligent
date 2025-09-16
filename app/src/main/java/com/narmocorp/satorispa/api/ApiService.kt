package com.narmocorp.satorispa.api

import com.narmocorp.satorispa.models.Usuario
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface ApiService {
    // GET /usuarios
    @GET("usuarios")
    fun getUsuarios(): Call<List<Usuario>>

    //mandar llamar el endpoint de login
    // POST /login
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}

// Si no existen, agrega estas clases:
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val token: String? = null,
    val usuario: Usuario? = null,
    val message: String? = null
)
