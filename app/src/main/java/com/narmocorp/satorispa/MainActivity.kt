package com.narmocorp.satorispa

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.narmocorp.satorispa.controller.loginUser
import com.narmocorp.satorispa.ui.theme.SatoriSPATheme
import com.narmocorp.satorispa.utils.SessionManager
import com.narmocorp.satorispa.views.*
import com.narmocorp.satorispa.views.cliente.ClientHomeScreen
import com.narmocorp.satorispa.views.cliente.ClienteServiciosScreen
import com.narmocorp.satorispa.views.terapeuta.ConfigScreen
import com.narmocorp.satorispa.views.terapeuta.TerapeutaCambiarContrasenaScreen
import com.narmocorp.satorispa.views.terapeuta.TerapeutaCitasScreen
import com.narmocorp.satorispa.views.terapeuta.TerapeutaHomeScreen
import com.narmocorp.satorispa.views.terapeuta.TerapeutaPerfilScreen

class MainActivity : FragmentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(this, "El permiso para acceder a la galería es necesario para cambiar la foto de perfil.", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeAppCheck()
        requestGalleryPermission()

        enableEdgeToEdge()
        setContent {
            SatoriSPATheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                NavHost(navController = navController, startDestination = "auth_gate") {
                    composable("auth_gate") {
                        // Dibuja la misma UI que la pantalla de inicio para evitar el fondo blanco.
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = R.drawable.fondo),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Logo Satori Spa",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(200.dp)
                            )
                        }

                        val sessionManager = SessionManager(context)
                        val user = FirebaseAuth.getInstance().currentUser

                        LaunchedEffect(Unit) {
                            if (user != null && sessionManager.getSessionPreference()) {
                                showBiometricPrompt { navigateToUserHome(navController) }
                            } else {
                                navController.navigate("start") {
                                    popUpTo("auth_gate") { inclusive = true }
                                }
                            }
                        }
                    }

                    composable("start") {
                        StartScreen(
                            onServicesClick = { navController.navigate("services") },
                            onRegisterClick = { navController.navigate("register") },
                            onLoginClick = { navController.navigate("login")}
                        )
                    }

                    // Servicios como invitado (desde start screen)
                    composable("services") {
                        ServicesScreen(
                            navController = navController,
                            isGuest = true
                        )
                    }

                    composable("login") {
                        Login(
                            emailLabel = "Correo electrónico",
                            onLogin = { email, password, keepSession ->
                                loginUser(email, password, keepSession, context, navController) { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            },
                            navController = navController
                        )
                    }

                    composable("register") {
                        Register(navController = navController)
                    }

                    composable("forgot_password") {
                        ForgotPasswordFlow(navController = navController)
                    }

                    composable("cliente_home") {
                        ClientHomeScreen(
                            onNavigateToNotifications = { navController.navigate("notificaciones") },
                            navController = navController,
                            onNavigateToConfig = { navController.navigate("configuracion") },
                            selectedRoute = "inicio",
                            onHomeClick = { navController.navigate("cliente_home") },
                            onServiciosClick = { navController.navigate("cliente_servicios") },
                            onCitasClick = { /* TODO: navegar a citas */ }
                        )
                    }

                    composable("cliente_servicios") {
                        ClienteServiciosScreen(
                            navController = navController,
                            onNavigateToNotifications = { navController.navigate("notificaciones") },
                            onNavigateToConfig = { navController.navigate("configuracion") }
                        )
                    }

                    composable("terapeuta_home") {
                        TerapeutaHomeScreen(
                            onNavigateToConfig = { navController.navigate("terapeuta_config") },
                            onCitasClick = { navController.navigate("terapeuta_citas") }
                        )
                    }

                    composable("terapeuta_config") {
                        ConfigScreen(navController = navController)
                    }

                    composable("terapeuta_perfil") {
                        TerapeutaPerfilScreen(navController = navController)
                    }

                    composable("terapeuta_cambiar_contrasena") {
                        TerapeutaCambiarContrasenaScreen(navController = navController)
                    }



                    composable("terapeuta_citas") {
                        TerapeutaCitasScreen(navController = navController)
                    }

                    composable("notificaciones") {
                        NotificacionesScreen(navController = navController)
                    }

                    composable("configuracion") {
                        ConfiguracionScreen(navController = navController)
                    }

                    composable("editar_perfil") {
                        EditarPerfilScreen(navController = navController)
                    }

                    composable("cambiar_contrasena") {
                        CambiarContrasenaScreen(navController = navController)
                    }

                    composable("terminos_condiciones") {
                        TerminosCondicionesScreen(navController = navController)
                    }

                    composable("politica_privacidad") {
                        PoliticaPrivacidadScreen(navController = navController)
                    }
                }
            }
        }
    }

    private fun requestGalleryPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun initializeAppCheck() {
        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
    }

    private fun navigateToUserHome(navController: NavController) {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val user = auth.currentUser

        if (user == null) {
            navController.navigate("start") { popUpTo("auth_gate") { inclusive = true } }
            return
        }

        db.collection("usuarios").document(user.uid).get()
            .addOnSuccessListener { document ->
                val rol = document?.getString("rol")?.trim()?.lowercase()
                val destination = when (rol) {
                    "cliente" -> "cliente_home"
                    "terapeuta" -> "terapeuta_home"
                    else -> "start" // Fallback
                }
                navController.navigate(destination) { popUpTo("auth_gate") { inclusive = true } }
            }
            .addOnFailureListener {
                Log.e("MainActivity", "Error getting user role", it)
                navController.navigate("start") { popUpTo("auth_gate") { inclusive = true } }
            }
    }

    private fun showBiometricPrompt(onSuccess: () -> Unit) {
        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS) {
            val executor = ContextCompat.getMainExecutor(this)
            val biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        onSuccess()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        finish()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                    }
                })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Inicio de Sesión Biométrico")
                .setSubtitle("Inicia sesión con tu huella o Face ID")
                .setNegativeButtonText("Cancelar")
                .build()

            biometricPrompt.authenticate(promptInfo)
        } else {
            onSuccess()
        }
    }
}
