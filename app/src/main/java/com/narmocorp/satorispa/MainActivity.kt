package com.narmocorp.satorispa

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.narmocorp.satorispa.controller.loginUser
import com.narmocorp.satorispa.ui.theme.SatoriSPATheme
import com.narmocorp.satorispa.views.ForgotPasswordFlow
import com.narmocorp.satorispa.views.Login
import com.narmocorp.satorispa.views.Register
import com.narmocorp.satorispa.views.ServicesScreen
import com.narmocorp.satorispa.views.ConfiguracionScreen
import com.narmocorp.satorispa.views.EditarPerfilScreen
import com.narmocorp.satorispa.views.CambiarContrasenaScreen
import com.narmocorp.satorispa.views.TerminosCondicionesScreen
import com.narmocorp.satorispa.views.PoliticaPrivacidadScreen
import com.narmocorp.satorispa.views.cliente.ClientHomeScreen
import com.narmocorp.satorispa.views.cliente.ClienteServiciosScreen
import com.narmocorp.satorispa.views.terapeuta.ConfigScreen
import com.narmocorp.satorispa.views.terapeuta.TerapeutaCambiarContrasenaScreen
import com.narmocorp.satorispa.views.terapeuta.TerapeutaCitasScreen
import com.narmocorp.satorispa.views.terapeuta.TerapeutaHomeScreen
import com.narmocorp.satorispa.views.terapeuta.TerapeutaPerfilScreen
import com.narmocorp.satorispa.views.NotificacionesScreen
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeAppCheck()

        enableEdgeToEdge()
        setContent {
            SatoriSPATheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                NavHost(navController = navController, startDestination = "start") {
                    composable("start") {
                        StartScreen(
                            onServicesClick = { navController.navigate("services") },
                            onRegisterClick = { navController.navigate("register") }
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
                            onLogin = { email, password ->
                                loginUser(email, password, navController) { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT)
                                        .show()
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

                    // =========================================================
                    // CLIENTE HOME - LÓGICA DE RECARGA ELIMINADA
                    // =========================================================
                    composable("cliente_home") {
                        // 🔑 Eliminamos toda la lógica de SavedStateHandle y shouldRefresh
                        ClientHomeScreen(
                            onNavigateToNotifications = { navController.navigate("notificaciones") },
                            navController = navController,
                            onNavigateToConfig = { navController.navigate("configuracion") },
                            selectedRoute = "inicio",
                            onHomeClick = { navController.navigate("cliente_home") },
                            onServiciosClick = { navController.navigate("cliente_servicios") },
                            onCitasClick = { /* TODO: navegar a citas */ }
                            // 🔑 Ya no pasamos shouldRefresh, la pantalla se actualiza sola
                        )
                    }

                    // Servicios para cliente autenticado (pantalla independiente con NavBar)
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
    private fun initializeAppCheck() {
        // 1. Asegúrate de que FirebaseApp esté inicializado
        // Usamos la inicialización explícita, que es más robusta
        FirebaseApp.initializeApp(this)

        // 2. Instala el proveedor de Play Integrity
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
    }
}