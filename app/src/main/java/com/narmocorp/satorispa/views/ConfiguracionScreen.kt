package com.narmocorp.satorispa.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ConfiguracionScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD3B8A5), shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color(0xFF6D4C41))
                }
                Text(
                    text = "Configuración",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6D4C41),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        var modoOscuro by remember { mutableStateOf(false) }

        val textOnBackground = MaterialTheme.colorScheme.onBackground
        val textOnSurface = MaterialTheme.colorScheme.onSurface

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Sección: Cuenta
            SeccionTitulo("Cuenta", color = textOnBackground)
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

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de cerrar sesión
            Button(
                onClick = {
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
