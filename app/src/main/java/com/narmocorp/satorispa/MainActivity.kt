package com.narmocorp.satorispa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.narmocorp.satorispa.ui.theme.SATORISPATheme
import com.narmocorp.satorispa.views.Login
import com.narmocorp.satorispa.views.StartScreen
import com.narmocorp.satorispa.api.ApiService
import com.narmocorp.satorispa.api.LoginRequest
import com.narmocorp.satorispa.api.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SATORISPATheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "start") {
                    composable("start") {
                        StartScreen(
                            onLoginClick = { navController.navigate("login") }
                        )
                    }
                    composable("login") {
                        Login(
                            label1901 = "Correo electrónico",
                            onLogin = { correo, password ->
                                // Lógica de autenticación con tu API usando Retrofit
                                lifecycleScope.launch {
                                    val loginExitoso = loginUser(correo, password)
                                    if (loginExitoso) {
                                        navController.navigate("home")
                                    } else {
                                        // Aquí puedes mostrar un mensaje de error si lo deseas
                                    }
                                }
                            }
                        )
                    }
                    // composable("home") { HomeScreen() } // Si tienes una pantalla principal
                }
            }
        }
    }

    private suspend fun loginUser(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://TU_API_BASE_URL/") // Cambia esto por tu URL base real
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val api = retrofit.create(ApiService::class.java)
                val response = api.login(LoginRequest(email, password)).execute()
                response.isSuccessful && response.body()?.success == true
            } catch (e: Exception) {
                false
            }
        }
    }
}
