package com.narmocorp.satorispa

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.narmocorp.satorispa.controller.loginUser
import com.narmocorp.satorispa.ui.theme.SatoriSPATheme
import com.narmocorp.satorispa.utils.SessionManager
import com.narmocorp.satorispa.views.*
import com.narmocorp.satorispa.views.cliente.AgendarCitaScreen
import com.narmocorp.satorispa.views.cliente.ClientHomeScreen
import com.narmocorp.satorispa.views.cliente.ClienteServiciosScreen
import com.narmocorp.satorispa.views.cliente.MisCitasScreen
import com.narmocorp.satorispa.views.terapeuta.ConfigScreen
import com.narmocorp.satorispa.views.terapeuta.TerapeutaCambiarContrasenaScreen
import com.narmocorp.satorispa.views.terapeuta.TerapeutaCitasScreen
import com.narmocorp.satorispa.views.terapeuta.TerapeutaHomeScreen
import com.narmocorp.satorispa.views.terapeuta.TerapeutaPerfilScreen
import kotlinx.coroutines.delay
import java.util.concurrent.Executor

private const val TAG = "MainActivity"

class MainActivity : FragmentActivity() {

    private val requestMultiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val galleryPermission = getGalleryPermissionString()
            permissions[galleryPermission]?.let { isGranted ->
                if (!isGranted) {
                    Toast.makeText(
                        this,
                        "El permiso para acceder a la galería es necesario para cambiar la foto de perfil.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions[Manifest.permission.POST_NOTIFICATIONS]?.let { isGranted ->
                    if (isGranted) {
                        Log.d(TAG, "Notification permission GRANTED by user.")
                    } else {
                        Log.d(TAG, "Notification permission DENIED by user.")
                        Toast.makeText(this, "Las notificaciones estarán desactivadas.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeAppCheck()
        createNotificationChannels()
        requestPermissions()

        enableEdgeToEdge()
        setContent {
            SatoriSPATheme {
                var showCheckmarkAnimation by remember { mutableStateOf(false) }

                val context = LocalContext.current

                // Lógica para recibir el broadcast del envío de UID del NFC
                DisposableEffect(Unit) {
                    val receiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context?, intent: Intent?) {
                            if (intent?.action == "nfc-uid-sent-success") {
                                showCheckmarkAnimation = true
                            }
                        }
                    }
                    val filter = IntentFilter("nfc-uid-sent-success")
                    LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter)

                    onDispose {
                        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
                    }
                }

                // Ocultar la animación después de unos segundos
                LaunchedEffect(showCheckmarkAnimation) {
                    if (showCheckmarkAnimation) {
                        delay(2500) // Muestra la animación por 2.5 segundos
                        showCheckmarkAnimation = false
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        val navController = rememberNavController()
                        // ... Tu NavHost va aquí adentro ...
                        NavHost(navController = navController, startDestination = "auth_gate") {
                            composable("auth_gate") {
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
                            // ... Todas tus otras rutas ...
                            composable("start") {
                                StartScreen(
                                    onServicesClick = { navController.navigate("services") },
                                    onRegisterClick = { navController.navigate("register") },
                                    onLoginClick = { navController.navigate("login")}
                                )
                            }

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

                            composable("cliente_home") {
                                ClientHomeScreen(
                                    onNavigateToNotifications = { navController.navigate("notificaciones") },
                                    navController = navController,
                                    onNavigateToConfig = { navController.navigate("configuracion") },
                                    selectedRoute = "inicio",
                                    onHomeClick = { navController.navigate("cliente_home") },
                                    onServiciosClick = { navController.navigate("cliente_servicios") },
                                    onCitasClick = { navController.navigate("cliente_mis_citas") }
                                )
                            }

                            composable("cliente_servicios") {
                                ClienteServiciosScreen(
                                    navController = navController,
                                    onNavigateToNotifications = { navController.navigate("notificaciones") },
                                    onNavigateToConfig = { navController.navigate("configuracion") }
                                )
                            }
                            // 💡 NUEVA RUTA PARA AGENDAR CITA
                            composable(
                                route = "agendar_cita/{servicioId}",
                                arguments = listOf(navArgument("servicioId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val servicioId = backStackEntry.arguments?.getString("servicioId")
                                if (servicioId != null) {
                                    // Debes asegurarte de importar AgendarCitaScreen
                                    AgendarCitaScreen(
                                        navController = navController,
                                        servicioId = servicioId
                                    )
                                } else {
                                    // Manejar el error si el ID no está presente, volviendo a servicios
                                    navController.popBackStack()
                                }
                            }
                            composable("cliente_mis_citas") {
                                MisCitasScreen(
                                    navController = navController,
                                    // 💡 PASAR LOS CALLBACKS AQUÍ:
                                    onNavigateToNotifications = { navController.navigate("notificaciones") },
                                    onNavigateToConfig = { navController.navigate("configuracion") }
                                )
                            }

                            composable("terapeuta_home") {
                                TerapeutaHomeScreen(
                                    onNavigateToNotifications = { navController.navigate("notificaciones") },
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

                            composable("ayuda") {
                                val user = FirebaseAuth.getInstance().currentUser
                                val db = FirebaseFirestore.getInstance()
                                var isTerapeuta by remember { mutableStateOf(false) } // Default to client

                                if (user != null) {
                                    // Use a LaunchedEffect to fetch the user role once
                                    LaunchedEffect(key1 = user.uid) {
                                        db.collection("usuarios").document(user.uid).get()
                                            .addOnSuccessListener { document ->
                                                if (document != null) {
                                                    val rol = document.getString("rol")?.trim()?.lowercase()
                                                    isTerapeuta = rol == "terapeuta"
                                                }
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.e(TAG, "Error getting user role for AyudaScreen", exception)
                                                // Keep default value (false) in case of error
                                            }
                                    }
                                }
                                AyudaScreen(navController = navController, isTerapeuta = isTerapeuta)
                            }

                            composable("acerca_de") {
                                AcercaDeScreen(navController = navController)
                            }
                        }

                        // Animación de palomita superpuesta
                        CheckmarkOverlay(visible = showCheckmarkAnimation)
                    }
                }
            }
        }
    }

    // ... resto de tus funciones de MainActivity ...
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Canal para Citas de Cliente (Confirmaciones)
            val citasChannel = NotificationChannel(
                "citas_channel",
                "Confirmación de Citas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones para nuevas citas agendadas."
            }

            // Canal para Citas de Terapeuta
            val citasTerapeutaChannel = NotificationChannel(
                "citas_terapeuta_channel",
                "Nuevas Citas (Terapeutas)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifica a los terapeutas sobre nuevas citas asignadas."
            }

            // Canal para Recordatorios
            val recordatoriosChannel = NotificationChannel(
                "recordatorios_channel",
                "Recordatorios de Citas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Recordatorios para citas próximas."
            }

            // Canal General
            val generalChannel = NotificationChannel(
                "general_channel",
                "Notificaciones Generales",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones generales y anuncios."
            }

            notificationManager.createNotificationChannel(citasChannel)
            notificationManager.createNotificationChannel(citasTerapeutaChannel)
            notificationManager.createNotificationChannel(recordatoriosChannel)
            notificationManager.createNotificationChannel(generalChannel)
        }
    }

    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        val galleryPermission = getGalleryPermissionString()
        if (ContextCompat.checkSelfPermission(this, galleryPermission) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(galleryPermission)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestMultiplePermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    private fun getGalleryPermissionString(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
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
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            navController.navigate("start") {
                popUpTo("auth_gate") { inclusive = true }
            }
            return
        }

        FirebaseFirestore.getInstance().collection("usuarios").document(user.uid).get()
            .addOnSuccessListener { document ->
                val rol = document?.getString("rol")?.trim()?.lowercase()
                val destination = if (rol == "terapeuta") "terapeuta_home" else "cliente_home"
                navController.navigate(destination) {
                    popUpTo("auth_gate") { inclusive = true }
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting user role, defaulting to cliente_home", it)
                // Default to client home on failure
                navController.navigate("cliente_home") {
                    popUpTo("auth_gate") { inclusive = true }
                }
            }
    }

    private fun showBiometricPrompt(onSuccess: () -> Unit) {
        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) != BiometricManager.BIOMETRIC_SUCCESS) {
            onSuccess() // Si no se puede usar biométrico, simplemente navega.
            return
        }

        val executor: Executor = ContextCompat.getMainExecutor(this)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verificación biométrica")
            .setSubtitle("Confirma tu identidad para continuar")
            .setNegativeButtonText("Usar contraseña")
            .build()

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // El usuario canceló o hubo un error. Puedes manejarlo si es necesario.
                    Log.d(TAG, "Biometric auth error: $errString")
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }
}

@Composable
fun CheckmarkOverlay(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)), // Fondo semitransparente
            contentAlignment = Alignment.Center
        ) {
            val context = LocalContext.current
            // Obtiene el ID del recurso de forma dinámica para evitar problemas de caché del IDE
            val resourceId = remember(context) {
                context.resources.getIdentifier("success", "raw", context.packageName)
            }

            // Carga la animación desde el ID de recurso obtenido dinámicamente
            val composition by rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(resourceId)
            )

            // Muestra la animación
            LottieAnimation(
                composition = composition,
                iterations = 1, // La animación se reproducirá una sola vez
                modifier = Modifier.size(250.dp) // Puedes ajustar el tamaño
            )
        }
    }
}
