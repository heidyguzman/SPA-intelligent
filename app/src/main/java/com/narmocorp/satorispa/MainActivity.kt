package com.narmocorp.satorispa

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.narmocorp.satorispa.controllers.LoginController
import com.narmocorp.satorispa.models.Usuario
import com.narmocorp.satorispa.ui.theme.SATORISPATheme
import com.narmocorp.satorispa.views.Inicio
import com.narmocorp.satorispa.views.Login
import com.narmocorp.satorispa.views.StartScreen
import com.narmocorp.satorispa.views.Notifications
import com.narmocorp.satorispa.views.Register
import com.narmocorp.satorispa.views.Servicios
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class MainActivity : FragmentActivity() {

    private lateinit var executor: Executor

    private fun showBiometricPrompt(
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (Int, CharSequence) -> Unit,
        onFailed: () -> Unit
    ) {
        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) != BiometricManager.BIOMETRIC_SUCCESS) {
            onError(-1, "No hay hardware de biometría disponible")
            return
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Inicio de sesión biométrico")
            .setSubtitle("Inicia sesión con tu huella digital")
            .setNegativeButtonText("Usar contraseña")
            .build()

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        executor = ContextCompat.getMainExecutor(this)

        val sharedPreferences = getSharedPreferences("SatoriSPA_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val userJson = sharedPreferences.getString("usuario", null)
        val savedUser = userJson?.let { gson.fromJson(it, Usuario::class.java) }

        setContent {
            SATORISPATheme {
                val navController = rememberNavController()
                var usuarioLogueado by remember { mutableStateOf(savedUser) }

                NavHost(
                    navController = navController,
                    startDestination = "start",
                ) {
                    composable("start") {
                        StartScreen(
                            onLoginClick = {
                                // FUNCIONALIDAD DE TU COMPAÑERO: Biometría si ya hay usuario guardado
                                if (usuarioLogueado != null) {
                                    showBiometricPrompt(
                                        onSuccess = { _ ->
                                            navController.navigate("inicio") {
                                                popUpTo("start") { inclusive = true }
                                            }
                                        },
                                        onError = { errorCode, errString ->
                                            Log.d("BiometricError", "Code: $errorCode, Msg: $errString")
                                            navController.navigate("login")
                                        },
                                        onFailed = {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Huella no reconocida",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                } else {
                                    navController.navigate("login")
                                }
                            },
                            onRegisterClick = { navController.navigate("register") }
                        )
                    }

                    composable("login") {
                        Login(
                            label1901 = "Correo electrónico",
                            // FUNCIONALIDAD DE TU COMPAÑERO: Login con keepSession
                            onLogin = { correo, contrasena, keepSession ->
                                lifecycleScope.launch {
                                    try {
                                        val usuario = LoginController.loginUser(correo, contrasena)
                                        if (usuario != null) {
                                            usuarioLogueado = usuario

                                            // FUNCIONALIDAD DE TU COMPAÑERO: Guardar sesión
                                            if (keepSession) {
                                                val userJsonToSave = gson.toJson(usuario)
                                                sharedPreferences.edit {
                                                    putString("usuario", userJsonToSave)
                                                }
                                            } else {
                                                sharedPreferences.edit { remove("usuario") }
                                            }

                                            Toast.makeText(
                                                this@MainActivity,
                                                "Login exitoso: ${usuario.nombre}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            Log.d("MainActivity", "Usuario: $usuario")
                                            navController.navigate("inicio") {
                                                popUpTo("start") { inclusive = true }
                                            }
                                        } else {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Error en el login",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            Log.d("MainActivity", "Error en el login")
                                        }
                                    } catch (e: Exception) {
                                        Log.e("LoginError", "Excepción durante el login: ${e.message}")
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Error de conexión",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            },
                            navController = navController
                        )
                    }

                    composable("inicio") {
                        Inicio(
                            usuario = usuarioLogueado,
                            selectedRoute = "inicio",
                            onNavigateToNotifications = {
                                navController.navigate("notifications")
                            },
                            onNavigateToConfig = {
                                navController.navigate("configuracion")
                            },
                            onHomeClick = {
                                navController.navigate("inicio")
                            },
                            onServiciosClick = {
                                navController.navigate("servicios")
                            },
                            onCitasClick = {
                                navController.navigate("citas")
                            }
                        )
                    }

                    composable("servicios") {
                        Servicios(
                            selectedRoute = "servicios",
                            onHomeClick = {
                                navController.navigate("inicio")
                            },
                            onServiciosClick = {
                                navController.navigate("servicios")
                            },
                            onCitasClick = {
                                navController.navigate("citas")
                            },
                            onNavigateToNotifications = {
                                navController.navigate("notifications")
                            },
                            onNavigateToConfig = {
                                navController.navigate("configuracion")
                            }
                        )
                    }

                    // TU FUNCIONALIDAD: Sistema de notificaciones
                    composable("notifications") {
                        val notifications = Notifications()
                        notifications.Notificaciones(
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("register") {
                        Register(navController = navController)
                    }

                    composable("configuracion") {
                        com.narmocorp.satorispa.views.Configuracion(
                            usuario = usuarioLogueado,
                            onBack = {
                                navController.navigate("inicio") {
                                    popUpTo("configuracion") { inclusive = true }
                                }
                            },
                            onLogout = {
                                // 1️⃣ Eliminar datos de sesión guardados
                                sharedPreferences.edit { remove("usuario") }

                                // 2️⃣ Borrar variable local
                                usuarioLogueado = null

                                // 3️⃣ Redirigir al login y limpiar historial de navegación
                                navController.navigate("login") {
                                    // Elimina todo lo anterior en el stack
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                                }

                                // 4️⃣ Mostrar mensaje
                                Toast.makeText(
                                    this@MainActivity,
                                    "Sesión cerrada correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            }
        }
    }
}