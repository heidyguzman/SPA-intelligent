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
    val primaryBrandColor = Color(0xff995d2d)
    val secondaryBrandColor = Color(0xffdbbba6)
    val tertiaryBrandColor = Color(0xffb08d73)
    val textOnSecondaryPlatform = Color(0xff71390c)

    var notificacionesActivas by remember { mutableStateOf(true) }
    var modoOscuro by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                SeccionTitulo("Cuenta")
                Spacer(modifier = Modifier.height(8.dp))

                OpcionConfiguracion(
                    icono = Icons.Default.Person,
                    titulo = "Perfil",
                    subtitulo = "Editar información personal",
                    onClick = { navController.navigate("editar_perfil") }
                )

                OpcionConfiguracion(
                    icono = Icons.Default.Lock,
                    titulo = "Cambiar contraseña",
                    subtitulo = "Actualizar credenciales de acceso",
                    onClick = { navController.navigate("cambiar_contrasena") }
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
                SeccionTitulo("Información")
                Spacer(modifier = Modifier.height(8.dp))

                OpcionConfiguracion(
                    icono = Icons.Default.Description,
                    titulo = "Términos y condiciones",
                    subtitulo = "Políticas de uso",
                    onClick = { navController.navigate("terminos_condiciones") }
                )

                OpcionConfiguracion(
                    icono = Icons.Default.PrivacyTip,
                    titulo = "Política de privacidad",
                    subtitulo = "Cómo usamos tus datos",
                    onClick = { navController.navigate("politica_privacidad") }
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
                        containerColor = primaryBrandColor
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
fun SeccionTitulo(texto: String) {
    Text(
        text = texto,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xff71390c),
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
fun OpcionConfiguracion(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xfff5f5f5))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = Color(0xff995d2d),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xff71390c)
            )
            Text(
                text = subtitulo,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}
/*
@Composable
fun OpcionConSwitch(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xfff5f5f5))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = Color(0xff995d2d),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xff71390c)
            )
            Text(
                text = subtitulo,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xff995d2d),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}*/