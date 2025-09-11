package com.narmocorp.satorispa.api

import com.narmocorp.satorispa.models.Usuario
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    // GET /usuarios
    @GET("usuarios")
    fun getUsuarios(): Call<List<Usuario>>
}