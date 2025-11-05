package com.narmocorp.satorispa.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PoliticaPrivacidadScreen(navController: NavController) {
    // CORRECCIÓN: Usar MaterialTheme.colorScheme en lugar de colores fijos
    val primaryBrandColor = MaterialTheme.colorScheme.primary
    val secondaryBrandColor = MaterialTheme.colorScheme.secondary
    val textOnSecondaryPlatform = MaterialTheme.colorScheme.onSecondary // Blanco en Dark Mode (Header)
    val textOnBackground = MaterialTheme.colorScheme.onBackground       // Blanco en Dark Mode (Título/Texto de Card)
    val textColorBody = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f) // Texto del cuerpo

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Usa el color de fondo del tema
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
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
                    "Política de Privacidad",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textOnSecondaryPlatform
                )
            }

            // Contenido
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Icono decorativo (Diseño original restaurado)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .background(secondaryBrandColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.PrivacyTip,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = primaryBrandColor
                        )
                    }
                }

                Text(
                    "Última actualización: Octubre 2025",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), // Usa color del tema
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Introducción Card (Diseño original restaurado)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface // Usa color del tema
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Security,
                            contentDescription = null,
                            tint = primaryBrandColor,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "En Satori SPA, protegemos tu privacidad y datos personales con los más altos estándares de seguridad.",
                            fontSize = 14.sp,
                            color = textOnBackground, // << CORRECCIÓN: Usar onBackground
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Contenido de política
                SeccionPrivacidad(
                    titulo = "1. Información que Recopilamos",
                    contenido = "Recopilamos información personal que nos proporcionas directamente:\n\n" +
                            "• Información de cuenta: nombre, correo electrónico\n" +
                            "• Información de perfil: foto, preferencias, historial\n" +
                            "• Datos de NFC: identificación de la tarjeta o dispositivo asociado.",
                    titleColor = textOnBackground,
                    bodyColor = textColorBody
                )

                SeccionPrivacidad(
                    titulo = "2. Cómo Usamos tu Información",
                    contenido = "Utilizamos tus datos para:\n\n" +
                            "• Proporcionar y mejorar nuestros servicios\n" +
                            "• Enviar confirmaciones y recordatorios\n" +
                            "• Comunicarnos contigo sobre servicios\n" +
                            "• Validar accesos mediante NFC.\n" +
                            "• Cumplir con requisitos legales",
                    titleColor = textOnBackground,
                    bodyColor = textColorBody
                )

                SeccionPrivacidad(
                    titulo = "3. Compartir Información",
                    contenido = "Compartimos tu información solo cuando es necesario:\n\n" +
                            "• Con terapeutas: para coordinar servicios\n" +
                            "• Por requisitos legales: autoridades competentes\n\n" +
                            "Nunca vendemos tu información personal a terceros.",
                    titleColor = textOnBackground,
                    bodyColor = textColorBody
                )

                SeccionPrivacidad(
                    titulo = "4. Seguridad de Datos",
                    contenido = "Implementamos medidas de seguridad robustas:\n\n" +
                            "• Almacenamiento seguro en servidores protegidos\n" +
                            "• Acceso restringido a información personal\n" +
                            "• Monitoreo continuo de seguridad\n" +
                            "• Auditorías regulares de sistemas\n" +
                            "• Cumplimiento con estándares internacionales",
                    titleColor = textOnBackground,
                    bodyColor = textColorBody
                )

                SeccionPrivacidad(
                    titulo = "5. Tus Derechos",
                    contenido = "Tienes derecho a:\n\n" +
                            "• Acceder a tus datos personales\n" +
                            "• Corregir información inexacta\n" +
                            "• Solicitar eliminación de tu cuenta\n" +
                            "• Oponerte al procesamiento de datos\n" +
                            "• Portabilidad de tus datos\n" +
                            "• Retirar consentimiento en cualquier momento\n\n" ,
                    titleColor = textOnBackground,
                    bodyColor = textColorBody
                )

                SeccionPrivacidad(
                    titulo = "6. Cookies y Tecnologías Similares",
                    contenido = "Utilizamos cookies para:\n\n" +
                            "• Mantener tu sesión activa\n" +
                            "• Recordar preferencias\n" +
                            "• Analizar uso de la aplicación\n" +
                            "• Mejorar funcionalidad\n\n" +
                            "Puedes gestionar cookies en la configuración de tu dispositivo.",
                    titleColor = textOnBackground,
                    bodyColor = textColorBody
                )

                SeccionPrivacidad(
                    titulo = "7. Retención de Datos",
                    contenido = "Conservamos tus datos personales:\n\n" +
                            "• Mientras mantengas una cuenta activa\n" +
                            "• El tiempo necesario para proporcionar servicios\n" +
                            "• Según lo requiera la ley (registros fiscales, etc.)\n" +
                            "• Hasta que solicites eliminación\n\n" +
                            "Datos anonimizados pueden conservarse para análisis estadísticos.",
                    titleColor = textOnBackground,
                    bodyColor = textColorBody
                )

                SeccionPrivacidad(
                    titulo = "8. Privacidad de Menores",
                    contenido = "Nuestros servicios están dirigidos a personas mayores de 18 años. No recopilamos intencionalmente información de menores sin consentimiento parental.",
                    titleColor = textOnBackground,
                    bodyColor = textColorBody
                )


                SeccionPrivacidad(
                    titulo = "9. Cambios a esta Política",
                    contenido = "Podemos actualizar esta política periódicamente. Te notificaremos cambios importantes por email o mediante aviso en la aplicación. El uso continuado después de cambios constituye aceptación.",
                    titleColor = textOnBackground,
                    bodyColor = textColorBody
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card de compromiso (Diseño original restaurado)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = secondaryBrandColor.copy(alpha = 0.3f) // Color claro base
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Security,
                            contentDescription = null,
                            tint = primaryBrandColor,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Tu confianza es nuestra prioridad",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = textOnBackground // << CORRECCIÓN: Usar onBackground
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Protegemos tus datos con los más altos estándares de seguridad y transparencia.",
                            fontSize = 13.sp,
                            color = textColorBody, // << CORRECCIÓN: Usar color del tema
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SeccionPrivacidad(titulo: String, contenido: String, titleColor: Color, bodyColor: Color) { // << CORRECCIÓN: Parámetros de color
    Column(modifier = Modifier.padding(bottom = 20.dp)) {
        Text(
            text = titulo,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor // << CORRECCIÓN: Usar color pasado
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = contenido,
            fontSize = 14.sp,
            color = bodyColor, // << CORRECCIÓN: Usar color pasado
            lineHeight = 22.sp
        )
    }
}