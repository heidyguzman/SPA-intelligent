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
import com.narmocorp.satorispa.api.RetrofitClient // Asegúrate que RetrofitClient está accesible
import com.narmocorp.satorispa.models.LoginRequest // Importa LoginRequest
import com.narmocorp.satorispa.ui.theme.SATORISPATheme
import com.narmocorp.satorispa.views.Login
import com.narmocorp.satorispa.views.StartScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                                // Iniciar corrutina para la llamada de red
                                lifecycleScope.launch {
                                    try {
                                        val loginRequest = LoginRequest(correo,contrasena)
                                        // Llamada a la API en el hilo de IO
                                        val response = withContext(Dispatchers.IO) {
                                            RetrofitClient.instance.loginUsuario(loginRequest.correo, loginRequest.contrasena).execute()
                                        }

                                        // Volver al hilo principal para actualizar la UI
                                        withContext(Dispatchers.Main) {
                                            if (response.isSuccessful) {
                                                val usuario = response.body()
                                                if (usuario != null) {
                                                    // Login exitoso
                                                    Toast.makeText(applicationContext, "Login exitoso: ${usuario.nombre}", Toast.LENGTH_LONG).show()
                                                    Log.d("MainActivity", "Login Exitoso: $usuario")
                                                    // Aquí podrías navegar a la pantalla de home:
                                                    // navController.navigate("home") {
                                                    //     popUpTo("login") { inclusive = true } // Para limpiar el backstack
                                                    // }
                                                } else {
                                                    // Respuesta exitosa pero cuerpo vacío (inesperado si el servidor siempre devuelve un usuario)
                                                    Toast.makeText(applicationContext, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show()
                                                    Log.e("MainActivity", "Login Exitoso pero cuerpo vacío")
                                                }
                                            } else {
                                                // Login fallido (credenciales incorrectas, error del servidor)
                                                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                                                Toast.makeText(applicationContext, "Error de login: $errorBody", Toast.LENGTH_LONG).show()
                                                Log.e("MainActivity", "Error de Login: ${response.code()} - $errorBody")
                                            }
                                        }
                                    } catch (e: Exception) {
                                        // Manejar excepciones de red u otras
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(applicationContext, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                                            Log.e("MainActivity", "Excepción en Login: ${e.message}", e)
                                        }
                                    }
                                }
                            }
                        )
                    }
                    // composable("home") { HomeScreen() } // Descomenta y define tu HomeScreen
                }
            }
        }
    }
}
