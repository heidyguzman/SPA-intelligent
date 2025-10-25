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

@Composable
fun ConfiguracionScreen(navController: NavController) {
    var modoOscuro by remember { mutableStateOf(false) }

    // Definir colores del tema para facilitar la corrección
    val secondaryBrandColor = MaterialTheme.colorScheme.secondary
    val textOnSecondaryPlatform = MaterialTheme.colorScheme.onSecondary // Blanco en Dark Mode (Header)
    val textOnBackground = MaterialTheme.colorScheme.onBackground       // Blanco en Dark Mode (Texto principal)
    val textOnSurface = MaterialTheme.colorScheme.onSurface             // Usado como base para subtítulos (Dark/Light)


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
                        Icons.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = textOnSecondaryPlatform, // Corregido
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Configuración",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textOnSecondaryPlatform // Corregido
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
                SeccionTitulo("Cuenta", color = textOnBackground) // << CORRECCIÓN DE COLOR
                Spacer(modifier = Modifier.height(8.dp))

                OpcionConfiguracion(
                    icono = Icons.Default.Person,
                    titulo = "Perfil",
                    subtitulo = "Editar información personal",
                    onClick = { navController.navigate("editar_perfil") },
                    textOnBackground = textOnBackground, // << CORRECCIÓN DE COLOR
                    textOnSurface = textOnSurface // << CORRECCIÓN DE COLOR
                )

                OpcionConfiguracion(
                    icono = Icons.Default.Lock,
                    titulo = "Cambiar contraseña",
                    subtitulo = "Actualizar credenciales de acceso",
                    onClick = { navController.navigate("cambiar_contrasena") },
                    textOnBackground = textOnBackground, // << CORRECCIÓN DE COLOR
                    textOnSurface = textOnSurface // << CORRECCIÓN DE COLOR
                )

                Spacer(modifier = Modifier.height(24.dp))
                /*
                // Sección: Preferencias
                SeccionTitulo("Preferencias")
                Spacer(modifier = Modifier.height(8.dp))

                OpcionConSwitch(
                    icono = Icons.Default.Notifications,
                    titulo = "Notificaciones",
                    subtitulo = "Recibir alertas y recordatorios",
                    checked = notificacionesActivas,
                    onCheckedChange = { notificacionesActivas = it }
                )

                OpcionConSwitch(
                    icono = Icons.Default.DarkMode,
                    titulo = "Modo oscuro",
                    subtitulo = "Tema oscuro para la aplicación",
                    checked = modoOscuro,
                    onCheckedChange = { modoOscuro = it }
                )*/

                Spacer(modifier = Modifier.height(24.dp))

                // Sección: Información
                SeccionTitulo("Información", color = textOnBackground) // << CORRECCIÓN DE COLOR
                Spacer(modifier = Modifier.height(8.dp))

                OpcionConfiguracion(
                    icono = Icons.Default.Description,
                    titulo = "Términos y condiciones",
                    subtitulo = "Políticas de uso",
                    onClick = { navController.navigate("terminos_condiciones") },
                    textOnBackground = textOnBackground, // << CORRECCIÓN DE COLOR
                    textOnSurface = textOnSurface // << CORRECCIÓN DE COLOR
                )

                OpcionConfiguracion(
                    icono = Icons.Default.PrivacyTip,
                    titulo = "Política de privacidad",
                    subtitulo = "Cómo usamos tus datos",
                    onClick = { navController.navigate("politica_privacidad") },
                    textOnBackground = textOnBackground, // << CORRECCIÓN DE COLOR
                    textOnSurface = textOnSurface // << CORRECCIÓN DE COLOR
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de cerrar sesión
                Button(
                    onClick = {
                        // TODO: Lógica de cerrar sesión
                        navController.navigate("start") {
                            popUpTo("start") { inclusive = true }
                        }
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
                        Icons.Default.Logout,
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
}

@Composable
fun SeccionTitulo(texto: String, color: Color) { // << FIRMA DE FUNCIÓN MODIFICADA
    Text(
        text = texto,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = color, // << CORRECCIÓN APLICADA
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
fun OpcionConfiguracion(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit,
    textOnBackground: Color, // << PARÁMETRO DE COLOR AÑADIDO
    textOnSurface: Color     // << PARÁMETRO DE COLOR AÑADIDO
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
                color = textOnBackground // << CORRECCIÓN APLICADA
            )
            Text(
                text = subtitulo,
                fontSize = 13.sp,
                color = textOnSurface.copy(alpha = 0.6f) // Subtítulo en gris (visible en ambos)
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
/*
@Composable
// ... (OpcionConSwitch y el resto del código)
*/