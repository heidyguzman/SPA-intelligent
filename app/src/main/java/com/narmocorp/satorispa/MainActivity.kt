package com.narmocorp.satorispa

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.narmocorp.satorispa.ui.theme.SATORISPATheme
import com.narmocorp.satorispa.views.Login
import com.narmocorp.satorispa.views.StartScreen
import com.narmocorp.satorispa.views.Inicio
import com.narmocorp.satorispa.views.Notifications
import com.narmocorp.satorispa.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.runtime.*
import com.narmocorp.satorispa.api.RetrofitClient
import com.narmocorp.satorispa.models.LoginRequest
import com.narmocorp.satorispa.controllers.LoginController

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SATORISPATheme {
                val navController = rememberAnimatedNavController()

                AnimatedNavHost(
                    navController = navController,
                    startDestination = "start",
                    enterTransition = { fadeIn(animationSpec = tween(500)) },
                    exitTransition = { fadeOut(animationSpec = tween(500)) },
                    popEnterTransition = { fadeIn(animationSpec = tween(500)) },
                    popExitTransition = { fadeOut(animationSpec = tween(500)) }
                ) {
                    composable("start") {
                        StartScreen(
                            onLoginClick = { navController.navigate("login") },
                            onRegisterClick = { navController.navigate("register") }
                        )
                    }
                    composable("login") {
                        Login(
                            label1901 = "Correo electronico",
                            onLogin = {
                                    correo, contrasena ->
                                lifecycleScope.launch {
                                    try {
                                        val usuario = LoginController.loginUser(correo, contrasena)
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
                                    } catch (e: Exception) {
                                        println("ExcepciÃ³n durante el login: ${e.message}")
                                    }
                                }

                            },
                            navController = navController // Se pasa el navController
                        )
                    }
                    composable("inicio") {
                        Inicio(
                            onNavigateToNotifications = {
                                navController.navigate("notifications")
                            }
                        )
                    }
                    composable("notifications") {
                        val notifications = Notifications()
                        notifications.Notificaciones(
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("register") {
                        com.narmocorp.satorispa.views.Register(navController = navController)
                    }
                }
            }
        }
    }
}