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
import com.narmocorp.satorispa.StartScreen
import com.narmocorp.satorispa.views.ConfiguracionScreen
import com.narmocorp.satorispa.views.EditarPerfilScreen
import com.narmocorp.satorispa.views.CambiarContrasenaScreen
import com.narmocorp.satorispa.views.TerminosCondicionesScreen
import com.narmocorp.satorispa.views.PoliticaPrivacidadScreen
import com.narmocorp.satorispa.views.cliente.ClientHomeScreen
import com.narmocorp.satorispa.views.cliente.ClienteServiciosScreen
import com.narmocorp.satorispa.views.terapeuta.TerapeutaHomeScreen
import com.narmocorp.satorispa.views.NotificacionesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

                    // Cliente Home - con navegación integrada
                    composable("cliente_home") {
                        ClientHomeScreen(
                            onNavigateToNotifications = { navController.navigate("notificaciones") },
                            onNavigateToConfig = { navController.navigate("configuracion") },
                            selectedRoute = "inicio",
                            onHomeClick = { navController.navigate("cliente_home") },
                            onServiciosClick = { navController.navigate("cliente_servicios") },
                            onCitasClick = { /* TODO: navegar a citas */ }
                        )
                    }

                    // Servicios para cliente autenticado (pantalla independiente con NavBar)
                    composable("cliente_servicios") {
                        ClienteServiciosScreen(navController = navController)
                    }

                    composable("terapeuta_home") {
                        TerapeutaHomeScreen()
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
}