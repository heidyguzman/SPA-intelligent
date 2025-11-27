package com.narmocorp.satorispa.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun PoliticaPrivacidadScreen(navController: NavController) {
    val primaryBrandColor = MaterialTheme.colorScheme.primary
    val secondaryBrandColor = MaterialTheme.colorScheme.secondary
    val textOnSecondaryPlatform = MaterialTheme.colorScheme.onSecondary
    val textOnBackground = MaterialTheme.colorScheme.onBackground
    val textColorBody = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header - SIN CAMBIOS
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
                // Icono decorativo - SIN CAMBIOS
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
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Introducción Card - SIN CAMBIOS
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
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
                            color = textOnBackground,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // NUEVO DISEÑO: Cards con Iconos
                CardPrivacidad(
                    numero = "1",
                    titulo = "Información que Recopilamos",
                    contenido = "Recopilamos información personal que nos proporcionas directamente:",
                    bullets = listOf(
                        "Información de cuenta: nombre, correo electrónico",
                        "Información de perfil: foto, preferencias, historial",
                        "Datos de NFC: identificación de la tarjeta o dispositivo asociado"
                    ),
                    icono = Icons.Default.Info,
                    iconColor = Color(0xFF3B82F6),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardPrivacidad(
                    numero = "2",
                    titulo = "Cómo Usamos tu Información",
                    contenido = "Utilizamos tus datos para:",
                    bullets = listOf(
                        "Proporcionar y mejorar nuestros servicios",
                        "Enviar confirmaciones y recordatorios",
                        "Comunicarnos contigo sobre servicios",
                        "Validar accesos mediante NFC",
                        "Cumplir con requisitos legales"
                    ),
                    icono = Icons.Default.Settings,
                    iconColor = Color(0xFF8B5CF6),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardPrivacidad(
                    numero = "3",
                    titulo = "Compartir Información",
                    contenido = "Compartimos tu información solo cuando es necesario:",
                    bullets = listOf(
                        "Con terapeutas: para coordinar servicios",
                        "Por requisitos legales: autoridades competentes"
                    ),
                    nota = "Nunca vendemos tu información personal a terceros.",
                    icono = Icons.Default.Share,
                    iconColor = Color(0xFF10B981),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardPrivacidad(
                    numero = "4",
                    titulo = "Seguridad de Datos",
                    contenido = "Implementamos medidas de seguridad robustas:",
                    bullets = listOf(
                        "Almacenamiento seguro en servidores protegidos",
                        "Acceso restringido a información personal",
                        "Monitoreo continuo de seguridad",
                        "Auditorías regulares de sistemas",
                        "Cumplimiento con estándares internacionales"
                    ),
                    icono = Icons.Default.Security,
                    iconColor = Color(0xFFEF4444),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardPrivacidad(
                    numero = "5",
                    titulo = "Tus Derechos",
                    contenido = "Tienes derecho a:",
                    bullets = listOf(
                        "Acceder a tus datos personales",
                        "Corregir información inexacta",
                        "Solicitar eliminación de tu cuenta",
                        "Oponerte al procesamiento de datos",
                        "Portabilidad de tus datos",
                        "Retirar consentimiento en cualquier momento"
                    ),
                    icono = Icons.Default.Verified,
                    iconColor = Color(0xFF06B6D4),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardPrivacidad(
                    numero = "6",
                    titulo = "Cookies y Tecnologías Similares",
                    contenido = "Utilizamos cookies para:",
                    bullets = listOf(
                        "Mantener tu sesión activa",
                        "Recordar preferencias",
                        "Analizar uso de la aplicación",
                        "Mejorar funcionalidad"
                    ),
                    nota = "Puedes gestionar cookies en la configuración de tu dispositivo.",
                    icono = Icons.Default.Cookie,
                    iconColor = Color(0xFFF59E0B),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardPrivacidad(
                    numero = "7",
                    titulo = "Retención de Datos",
                    contenido = "Conservamos tus datos personales:",
                    bullets = listOf(
                        "Mientras mantengas una cuenta activa",
                        "El tiempo necesario para proporcionar servicios",
                        "Según lo requiera la ley (registros fiscales, etc.)",
                        "Hasta que solicites eliminación"
                    ),
                    nota = "Datos anonimizados pueden conservarse para análisis estadísticos.",
                    icono = Icons.Default.Timer,
                    iconColor = Color(0xFFA855F7),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardPrivacidad(
                    numero = "8",
                    titulo = "Privacidad de Menores",
                    contenido = "Nuestros servicios están dirigidos a personas mayores de 18 años. No recopilamos intencionalmente información de menores sin consentimiento parental.",
                    icono = Icons.Default.ChildCare,
                    iconColor = Color(0xFFF43F5E),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardPrivacidad(
                    numero = "9",
                    titulo = "Cambios a esta Política",
                    contenido = "Podemos actualizar esta política periódicamente. Te notificaremos cambios importantes por email o mediante aviso en la aplicación. El uso continuado después de cambios constituye aceptación.",
                    icono = Icons.Default.Update,
                    iconColor = Color(0xFF64748B),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card de compromiso - SIN CAMBIOS
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
                            color = textOnBackground
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Protegemos tus datos con los más altos estándares de seguridad y transparencia.",
                            fontSize = 13.sp,
                            color = textColorBody,
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
fun CardPrivacidad(
    numero: String,
    titulo: String,
    contenido: String,
    bullets: List<String>? = null,
    nota: String? = null,
    icono: ImageVector,
    iconColor: Color,
    textColor: Color,
    bodyColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            // Icono con fondo de color
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Contenido
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$numero. $titulo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = contenido,
                    fontSize = 14.sp,
                    color = bodyColor,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Justify
                )

                // Bullets si existen
                bullets?.let { bulletList ->
                    Spacer(modifier = Modifier.height(8.dp))
                    bulletList.forEach { bullet ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .size(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(iconColor)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = bullet,
                                fontSize = 14.sp,
                                color = bodyColor,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Nota adicional si existe
                nota?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        fontSize = 13.sp,
                        color = bodyColor,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Justify,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(iconColor.copy(alpha = 0.1f))
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}