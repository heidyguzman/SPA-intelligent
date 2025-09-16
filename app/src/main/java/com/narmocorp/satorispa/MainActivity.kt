package com.narmocorp.satorispa

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.narmocorp.satorispa.ui.theme.SATORISPATheme
import com.narmocorp.satorispa.views.Login
import com.narmocorp.satorispa.views.StartScreen
import com.narmocorp.satorispa.views.Inicio
import com.narmocorp.satorispa.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.runtime.*
import com.narmocorp.satorispa.api.RetrofitClient
import com.narmocorp.satorispa.models.LoginRequest
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
                            onLogin = { correo, contrasena ->
                                lifecycleScope.launch {
                                    try {
                                        val loginRequest = LoginRequest(correo, contrasena)
                                        val response = withContext(Dispatchers.IO) {
                                            RetrofitClient.instance.login(correo, contrasena).execute()
                                        }
                                        withContext(Dispatchers.Main) {
                                            if(response.isSuccessful) {


                                            val usuario = response.body()
                                            if (usuario != null) {
                                                Toast.makeText(
                                                    applicationContext,"Login exitoso: ${usuario.nombre}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                Log.d("MainActivity", "Usuario: $usuario")
                                                navController.navigate("inicio")
                                            } else {
                                                Toast.makeText(
                                                    applicationContext, "Error en el login",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                Log.d("MainActivity", "Error en el login")
                                            }
                                            }
                                            else {
                                                val errorBody = response.errorBody()?.string()
                                                Toast.makeText(
                                                    applicationContext, "Error en el login: $errorBody",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                Log.d("MainActivity", "Error en el login: ${response.code()}+$errorBody" )

                                            }
                                        }
                                    } catch (e: Exception) {
                                        // Manejar excepción de red u otros errores
                                        println("Excepción durante el login: ${e.message}")
                                    }

                                }

                            }
                        )
                    }
                    composable("inicio") {
                        Inicio()
                    }
                }
            }
        }
    }
}
