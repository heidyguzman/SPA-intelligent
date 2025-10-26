package com.narmocorp.satorispa.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.narmocorp.satorispa.controller.AuthController
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout

//MOVER ESTAS FUNCIONES AL INICIO PARA RESOLVER LOS UNRESOLVED REFERENCE

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
fun ConfiguracionScreen(navController: NavController) {
    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }

    // Definir colores del tema para facilitar la corrección
    val primaryBrandColor = MaterialTheme.colorScheme.primary       // << CORRECCIÓN: Definir primaryBrandColor
    val secondaryBrandColor = MaterialTheme.colorScheme.secondary
    val textOnSecondaryPlatform = MaterialTheme.colorScheme.onSecondary
    val textOnBackground = MaterialTheme.colorScheme.onBackground
    val textOnSurface = MaterialTheme.colorScheme.onSurface


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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Sección: Cuenta
                SeccionTitulo("Cuenta", color = textOnBackground) // << AHORA RESUELTO
                Spacer(modifier = Modifier.height(8.dp))

                OpcionConfiguracion(
                    icono = Icons.Default.Person,
                    titulo = "Perfil",
                    subtitulo = "Editar información personal",
                    onClick = { navController.navigate("editar_perfil") },
                    textOnBackground = textOnBackground,
                    textOnSurface = textOnSurface
                )

                OpcionConfiguracion(
                    icono = Icons.Default.Lock,
                    titulo = "Cambiar contraseña",
                    subtitulo = "Actualizar credenciales de acceso",
                    onClick = { navController.navigate("cambiar_contrasena") },
                    textOnBackground = textOnBackground,
                    textOnSurface = textOnSurface
                )

                Spacer(modifier = Modifier.height(24.dp))
                // ... (Sección Preferencias Comentada)
                Spacer(modifier = Modifier.height(24.dp))

                // Sección: Información
                SeccionTitulo("Información", color = textOnBackground) // << AHORA RESUELTO
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

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de cerrar sesión
                Button(
                    onClick = {
                        mostrarDialogoCerrarSesion = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar sesión", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Diálogo de confirmación para cerrar sesión (Colores Corregidos)
    if (mostrarDialogoCerrarSesion) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoCerrarSesion = false
            },
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error // Usar color error para advertencia
                )
            },
            title = {
                Text(
                    "¿Cerrar Sesión?",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface // Se adapta al fondo del diálogo
                )
            },
            text = {
                Text(
                    "Tendrás que volver a iniciar sesión para acceder a tu cuenta.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f) // Se adapta al fondo del diálogo
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogoCerrarSesion = false
                        AuthController.cerrarSesion()
                        navController.navigate("start") {
                            popUpTo("start") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error // Botón de acción destructiva
                    )
                ) {
                    // Texto sobre el color 'error' para asegurar contraste
                    Text("Sí, Cerrar Sesión", color = MaterialTheme.colorScheme.onError)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarDialogoCerrarSesion = false }
                ) {
                    // Texto con el color primario del tema
                    Text("Cancelar", color = MaterialTheme.colorScheme.primary)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface // Fondo del diálogo adaptativo
        )
    }
}