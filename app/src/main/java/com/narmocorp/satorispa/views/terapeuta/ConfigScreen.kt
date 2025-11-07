package com.narmocorp.satorispa.views.terapeuta

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.narmocorp.satorispa.controller.AuthController
import com.narmocorp.satorispa.views.OpcionConfiguracion

@Composable
fun SeccionTitulo(texto: String, color: Color) {
    Text(
        text = texto,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = color,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
fun OpcionConfiguracion(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit,
    textOnBackground: Color,
    textOnSurface: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = textOnBackground
            )
            Text(
                text = subtitulo,
                fontSize = 13.sp,
                color = textOnSurface.copy(alpha = 0.6f)
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = textOnSurface.copy(alpha = 0.6f),
            modifier = Modifier.size(20.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ConfigScreen(navController: NavController) {
    val context = LocalContext.current
    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }
    val colorEsquema = MaterialTheme.colorScheme

    val secondaryBrandColor = MaterialTheme.colorScheme.secondary
    val textOnSecondaryPlatform = MaterialTheme.colorScheme.onSecondary
    val textOnBackground = MaterialTheme.colorScheme.onBackground
    val textOnSurface = MaterialTheme.colorScheme.onSurface
    val scrollState = rememberScrollState()

    if (mostrarDialogoCerrarSesion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCerrarSesion = false },
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = colorEsquema.error,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("¿Cerrar Sesión?") },
            text = {
                Text(
                    "Tendrás que volver a iniciar sesión para acceder a tu cuenta.",
                    color = colorEsquema.onSurface.copy(alpha = 0.8f)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        Toast.makeText(context, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show()
                        navController.navigate("login") {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                        mostrarDialogoCerrarSesion = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorEsquema.error
                    )
                ) {
                    Text("Sí, Cerrar Sesión", color = colorEsquema.onError)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarDialogoCerrarSesion = false }
                ) {
                    Text("Cancelar", color = colorEsquema.primary)
                }
            },
            containerColor = colorEsquema.surface
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header con esquinas redondeadas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(secondaryBrandColor)
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = textOnSecondaryPlatform,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Configuración",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textOnSecondaryPlatform
                )
            }

            // Contenido scrolleable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
            ) {
                // Sección: Cuenta
                SeccionTitulo("Cuenta", color = textOnBackground)
                Spacer(modifier = Modifier.height(8.dp))

                OpcionConfiguracion(
                    icono = Icons.Default.Person,
                    titulo = "Perfil",
                    subtitulo = "Editar información personal",
                    onClick = { navController.navigate("terapeuta_perfil") },
                    textOnBackground = textOnBackground,
                    textOnSurface = textOnSurface
                )

                OpcionConfiguracion(
                    icono = Icons.Default.Lock,
                    titulo = "Cambiar contraseña",
                    subtitulo = "Actualizar credenciales de acceso",
                    onClick = { navController.navigate("terapeuta_cambiar_contrasena") },
                    textOnBackground = textOnBackground,
                    textOnSurface = textOnSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Sección: Información
                SeccionTitulo("Información", color = textOnBackground)
                Spacer(modifier = Modifier.height(8.dp))

                OpcionConfiguracion(
                    icono = Icons.Default.Description,
                    titulo = "Términos y condiciones",
                    subtitulo = "Políticas de uso",
                    onClick = { navController.navigate("terminos_condiciones") },
                    textOnBackground = textOnBackground,
                    textOnSurface = textOnSurface
                )

                OpcionConfiguracion(
                    icono = Icons.Default.PrivacyTip,
                    titulo = "Política de privacidad",
                    subtitulo = "Cómo usamos tus datos",
                    onClick = { navController.navigate("politica_privacidad") },
                    textOnBackground = textOnBackground,
                    textOnSurface = textOnSurface
                )

                OpcionConfiguracion(
                    icono = Icons.Default.Info, // Usamos un ícono de información
                    titulo = "Acerca de Satori Spa",
                    subtitulo = "Versión y detalles de la aplicación",
                    onClick = { navController.navigate("acerca_de") }, // Nueva ruta de navegación
                    textOnBackground = textOnBackground,
                    textOnSurface = textOnSurface
                )

                OpcionConfiguracion(
                    icono = Icons.Default.HelpOutline,
                    titulo = "Ayuda y Soporte",
                    subtitulo = "Preguntas frecuentes y contacto",
                    onClick = { navController.navigate("ayuda") },
                    textOnBackground = textOnBackground,
                    textOnSurface = textOnSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                // =======================================================
                // 🔑 SECCIÓN DE BOTONES DE ACCIÓN FINAL
                // =======================================================
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // BOTÓN CERRAR SESIÓN
                    Button(
                        onClick = { mostrarDialogoCerrarSesion = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar Sesión", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
