package com.narmocorp.satorispa.controllers

import com.narmocorp.satorispa.api.RetrofitClient
import com.narmocorp.satorispa.models.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

object LoginController {
    suspend fun loginUser(correo: String, contrasena: String): Usuario? {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<Usuario> = RetrofitClient.instance.login(correo, contrasena).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}

