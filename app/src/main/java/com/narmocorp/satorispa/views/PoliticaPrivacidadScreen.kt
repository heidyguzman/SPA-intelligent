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
    val primaryBrandColor = Color(0xff995d2d)
    val secondaryBrandColor = Color(0xffdbbba6)
    val textOnSecondaryPlatform = Color(0xff71390c)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                // Icono decorativo
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
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Introducción
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xfff5f5f5)
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
                            color = textOnSecondaryPlatform,
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
                            "• Información de uso: interacciones con la app"
                )

                SeccionPrivacidad(
                    titulo = "2. Cómo Usamos tu Información",
                    contenido = "Utilizamos tus datos para:\n\n" +
                            "• Proporcionar y mejorar nuestros servicios\n" +
                            "• Enviar confirmaciones y recordatorios\n" +
                            "• Personalizar tu experiencia\n" +
                            "• Comunicarnos contigo sobre servicios\n" +
                            "• Prevenir fraudes y garantizar seguridad\n" +
                            "• Cumplir con requisitos legales"
                )

                SeccionPrivacidad(
                    titulo = "3. Compartir Información",
                    contenido = "Compartimos tu información solo cuando es necesario:\n\n" +
                            "• Con terapeutas: para coordinar servicios\n" +
                            "• Con procesadores de pago: para transacciones\n" +
                            "• Con proveedores de servicios: analytics, hosting\n" +
                            "• Por requisitos legales: autoridades competentes\n\n" +
                            "Nunca vendemos tu información personal a terceros."
                )

                SeccionPrivacidad(
                    titulo = "4. Seguridad de Datos",
                    contenido = "Implementamos medidas de seguridad robustas:\n\n" +
                            "• Almacenamiento seguro en servidores protegidos\n" +
                            "• Acceso restringido a información personal\n" +
                            "• Monitoreo continuo de seguridad\n" +
                            "• Auditorías regulares de sistemas\n" +
                            "• Cumplimiento con estándares internacionales"
                )

                SeccionPrivacidad(
                    titulo = "5. Tus Derechos",
                    contenido = "Tienes derecho a:\n\n" +
                            "• Acceder a tus datos personales\n" +
                            "• Corregir información inexacta\n" +
                            "• Solicitar eliminación de tu cuenta\n" +
                            "• Oponerte al procesamiento de datos\n" +
                            "• Portabilidad de tus datos\n" +
                            "• Retirar consentimiento en cualquier momento\n\n" +
                            "Para ejercer estos derechos, contáctanos en privacidad@satorispa.com"
                )

                SeccionPrivacidad(
                    titulo = "6. Cookies y Tecnologías Similares",
                    contenido = "Utilizamos cookies para:\n\n" +
                            "• Mantener tu sesión activa\n" +
                            "• Recordar preferencias\n" +
                            "• Analizar uso de la aplicación\n" +
                            "• Mejorar funcionalidad\n\n" +
                            "Puedes gestionar cookies en la configuración de tu dispositivo."
                )

                SeccionPrivacidad(
                    titulo = "7. Retención de Datos",
                    contenido = "Conservamos tus datos personales:\n\n" +
                            "• Mientras mantengas una cuenta activa\n" +
                            "• El tiempo necesario para proporcionar servicios\n" +
                            "• Según lo requiera la ley (registros fiscales, etc.)\n" +
                            "• Hasta que solicites eliminación\n\n" +
                            "Datos anonimizados pueden conservarse para análisis estadísticos."
                )

                SeccionPrivacidad(
                    titulo = "8. Privacidad de Menores",
                    contenido = "Nuestros servicios están dirigidos a personas mayores de 18 años. No recopilamos intencionalmente información de menores sin consentimiento parental."
                )

                SeccionPrivacidad(
                    titulo = "9. Transferencias Internacionales",
                    contenido = "Tus datos pueden ser transferidos y procesados en servidores ubicados fuera de México, siempre bajo medidas de protección adecuadas según normativas internacionales."
                )

                SeccionPrivacidad(
                    titulo = "10. Cambios a esta Política",
                    contenido = "Podemos actualizar esta política periódicamente. Te notificaremos cambios importantes por email o mediante aviso en la aplicación. El uso continuado después de cambios constituye aceptación."
                )

                SeccionPrivacidad(
                    titulo = "11. Contacto",
                    contenido = "Para preguntas sobre privacidad o ejercer tus derechos:\n\n" +
                            "Email: privacidad@satorispa.com\n" +
                            "Teléfono: +52 33 1234 5678\n" +
                            "Dirección: Manzanillo, Colima, México\n\n" +
                            "Responsable de Protección de Datos: data@satorispa.com"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card de compromiso
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = secondaryBrandColor.copy(alpha = 0.3f)
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
                            color = textOnSecondaryPlatform
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Protegemos tus datos con los más altos estándares de seguridad y transparencia.",
                            fontSize = 13.sp,
                            color = Color.DarkGray,
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
fun SeccionPrivacidad(titulo: String, contenido: String) {
    Column(modifier = Modifier.padding(bottom = 20.dp)) {
        Text(
            text = titulo,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xff71390c)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = contenido,
            fontSize = 14.sp,
            color = Color.DarkGray,
            lineHeight = 22.sp
        )
    }
}