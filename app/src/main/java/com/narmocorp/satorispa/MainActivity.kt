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
                                if (usuarioLogueado != null) {
                                    showBiometricPrompt(
                                        onSuccess = {
                                            navController.navigate("inicio") { popUpTo("start") { inclusive = true } }
                                        },
                                        onError = { errorCode, errString ->
                                            Log.d("BiometricError", "Code: $errorCode, Msg: $errString")
                                            navController.navigate("login")
                                        },
                                        onFailed = {
                                            Toast.makeText(applicationContext, "Huella no reconocida", Toast.LENGTH_SHORT).show()
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
                            onLogin = { correo, contrasena, keepSession ->
                                lifecycleScope.launch {
                                    try {
                                        val usuario = LoginController.loginUser(correo, contrasena)
                                        if (usuario != null) {
                                            usuarioLogueado = usuario
                                            if (keepSession) {
                                                val userJsonToSave = gson.toJson(usuario)
                                                sharedPreferences.edit { putString("usuario", userJsonToSave) }
                                            } else {
                                                sharedPreferences.edit { remove("usuario") }
                                            }
                                            Toast.makeText(
                                                applicationContext,"Login exitoso: ${usuario.nombre}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            navController.navigate("inicio") { popUpTo("start") { inclusive = true } }
                                        } else {
                                            Toast.makeText(
                                                applicationContext, "Error en el login",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    } catch (e: Exception) {
                                        println("Excepción durante el login: ${e.message}")
                                    }
                                }
                            },
                            navController = navController
                        )
                    }
                    composable("inicio") {
                        Inicio(usuario = usuarioLogueado)
                    }
                    composable("register") {
                        com.narmocorp.satorispa.views.Register(navController = navController)
                    }
                }
            }
        }
    }
}
