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

    // POST /usuarios
    @POST("usuarios")
    fun registerUser(@Body usuario: Usuario): Call<Usuario>
}
