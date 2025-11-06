package com.narmocorp.satorispa.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info

// =========================================================================
// FUNCIÓN PRINCIPAL ACERCA DE SCREEN
// =========================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcercaDeScreen(navController: NavController) {
    val colorEsquema = MaterialTheme.colorScheme

    val secondaryBrandColor = MaterialTheme.colorScheme.secondary
    val textOnSecondaryPlatform = MaterialTheme.colorScheme.onSecondary
    val textOnBackground = MaterialTheme.colorScheme.onBackground
    val primaryColor = MaterialTheme.colorScheme.primary

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorEsquema.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header con esquinas redondeadas (similar a ConfiguracionScreen)
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
                    "Acerca de Satori Spa",
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
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Info, // Usar un ícono grande para la identidad de la app
                    contentDescription = "Logo de la aplicación",
                    tint = primaryColor,
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Satori Spa App",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textOnBackground
                )

                Text(
                    text = "Versión 1.0.0 (Build 20241105)",
                    fontSize = 14.sp,
                    color = textOnBackground.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Bienvenido a la aplicación móvil oficial de Satori Spa. Nuestro objetivo es ofrecerte una experiencia de bienestar y relajación inigualable, permitiéndote gestionar tus citas y tratamientos de manera sencilla.",
                    fontSize = 16.sp,
                    color = textOnBackground,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(24.dp))

                Divider(color = colorEsquema.onSurface.copy(alpha = 0.1f))

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "**Desarrollado por: Satori Spa**",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = textOnBackground
                )
                Text(
                    text = "Todos los derechos reservados © 2025",
                    fontSize = 14.sp,
                    color = textOnBackground.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(64.dp))

                // Aquí podrías añadir más información, enlaces o íconos de redes sociales
            }
        }
    }
}