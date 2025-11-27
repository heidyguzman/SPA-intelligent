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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TerminosCondicionesScreen(navController: NavController) {
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
                    "Términos y Condiciones",
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
                            Icons.Default.Description,
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

                // NUEVO DISEÑO: Cards con Iconos
                CardTermino(
                    numero = "1",
                    titulo = "Aceptación de los Términos",
                    contenido = "Al acceder y utilizar la aplicación Satori SPA, usted acepta estar sujeto a estos Términos y Condiciones. Si no está de acuerdo con alguna parte de estos términos, no debe utilizar nuestra aplicación.",
                    icono = Icons.Default.CheckCircle,
                    iconColor = Color(0xFF10B981),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardTermino(
                    numero = "2",
                    titulo = "Uso de la Aplicación",
                    contenido = "Satori SPA es una plataforma que conecta clientes con terapeutas profesionales para servicios de spa y bienestar. Los usuarios se comprometen a:",
                    bullets = listOf(
                        "Proporcionar información precisa y actualizada",
                        "Mantener la confidencialidad de su cuenta",
                        "No utilizar la aplicación para fines ilegales",
                        "Respetar a otros usuarios y terapeutas"
                    ),
                    icono = Icons.Default.Book,
                    iconColor = Color(0xFF3B82F6),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardTermino(
                    numero = "3",
                    titulo = "Registro y Cuenta",
                    contenido = "Para utilizar ciertos servicios, debe crear una cuenta. Usted es responsable de:",
                    bullets = listOf(
                        "Mantener su contraseña segura",
                        "Todas las actividades realizadas bajo su cuenta",
                        "Notificar inmediatamente cualquier uso no autorizado"
                    ),
                    icono = Icons.Default.Person,
                    iconColor = Color(0xFFA855F7),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardTermino(
                    numero = "4",
                    titulo = "Servicios y Reservas",
                    contenido = "Las reservas de servicios están sujetas a:",
                    bullets = listOf(
                        "Disponibilidad del terapeuta",
                        "Políticas de cancelación específicas",
                        "Confirmación previa a la cita"
                    ),
                    icono = Icons.Default.CalendarToday,
                    iconColor = Color(0xFFF97316),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardTermino(
                    numero = "5",
                    titulo = "Acceso mediante NFC",
                    contenido = "El acceso mediante tarjetas NFC:",
                    bullets = listOf(
                        "Permite validar accesos dentro del spa",
                        "El usuario es responsable del uso adecuado de su tarjeta NFC asociado a su cuenta"
                    ),
                    icono = Icons.Default.CreditCard,
                    iconColor = Color(0xFF06B6D4),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardTermino(
                    numero = "6",
                    titulo = "Responsabilidades",
                    contenido = "Satori SPA actúa como intermediario entre clientes y terapeutas. No somos responsables de:",
                    bullets = listOf(
                        "La calidad de los servicios proporcionados",
                        "Lesiones o daños durante el tratamiento",
                        "Cancelaciones por parte del terapeuta",
                        "Resultados específicos de los tratamientos"
                    ),
                    icono = Icons.Default.Shield,
                    iconColor = Color(0xFFEF4444),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardTermino(
                    numero = "7",
                    titulo = "Propiedad Intelectual",
                    contenido = "Todo el contenido de la aplicación, incluyendo textos, gráficos, logos e imágenes, es propiedad de Satori SPA y está protegido por leyes de propiedad intelectual.",
                    icono = Icons.Default.Description,
                    iconColor = Color(0xFF8B5CF6),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardTermino(
                    numero = "8",
                    titulo = "Modificaciones",
                    contenido = "Nos reservamos el derecho de modificar estos términos en cualquier momento. Los cambios entrarán en vigencia inmediatamente después de su publicación en la aplicación.",
                    icono = Icons.Default.Warning,
                    iconColor = Color(0xFFF59E0B),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardTermino(
                    numero = "9",
                    titulo = "Terminación",
                    contenido = "Podemos suspender o terminar su acceso a la aplicación por:",
                    bullets = listOf(
                        "Violación de estos términos",
                        "Comportamiento inapropiado",
                        "Fraude o actividades sospechosas",
                        "A nuestra discreción"
                    ),
                    icono = Icons.Default.Cancel,
                    iconColor = Color(0xFFF43F5E),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                CardTermino(
                    numero = "10",
                    titulo = "Ley Aplicable",
                    contenido = "Estos términos se rigen por las leyes de México. Cualquier disputa será resuelta en los tribunales de Manzanillo, México.",
                    icono = Icons.Default.Gavel,
                    iconColor = Color(0xFF64748B),
                    textColor = textOnBackground,
                    bodyColor = textColorBody
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card de aceptación - SIN CAMBIOS
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
                            Icons.Default.Description,
                            contentDescription = null,
                            tint = primaryBrandColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Al usar Satori SPA, aceptas estos términos y condiciones.",
                            fontSize = 13.sp,
                            color = textOnBackground,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun CardTermino(
    numero: String,
    titulo: String,
    contenido: String,
    bullets: List<String>? = null,
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
                    textAlign = androidx.compose.ui.text.style.TextAlign.Justify
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
                                textAlign = androidx.compose.ui.text.style.TextAlign.Justify,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}