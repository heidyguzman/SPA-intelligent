package com.narmocorp.satorispa.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TerminosCondicionesScreen(navController: NavController) {
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
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Contenido de términos
                SeccionTerminos(
                    titulo = "1. Aceptación de los Términos",
                    contenido = "Al acceder y utilizar la aplicación Satori SPA, usted acepta estar sujeto a estos Términos y Condiciones. Si no está de acuerdo con alguna parte de estos términos, no debe utilizar nuestra aplicación."
                )

                SeccionTerminos(
                    titulo = "2. Uso de la Aplicación",
                    contenido = "Satori SPA es una plataforma que conecta clientes con terapeutas profesionales para servicios de spa y bienestar. Los usuarios se comprometen a:\n\n" +
                            "• Proporcionar información precisa y actualizada\n" +
                            "• Mantener la confidencialidad de su cuenta\n" +
                            "• No utilizar la aplicación para fines ilegales\n" +
                            "• Respetar a otros usuarios y terapeutas"
                )

                SeccionTerminos(
                    titulo = "3. Registro y Cuenta",
                    contenido = "Para utilizar ciertos servicios, debe crear una cuenta. Usted es responsable de:\n\n" +
                            "• Mantener su contraseña segura\n" +
                            "• Todas las actividades realizadas bajo su cuenta\n" +
                            "• Notificar inmediatamente cualquier uso no autorizado"
                )

                SeccionTerminos(
                    titulo = "4. Servicios y Reservas",
                    contenido = "Las reservas de servicios están sujetas a:\n\n" +
                            "• Disponibilidad del terapeuta\n" +
                            "• Políticas de cancelación específicas\n" +
                            "• Confirmación previa a la cita"
                )

                SeccionTerminos(
                    titulo = "5. Pagos y Reembolsos",
                    contenido = "Los pagos se procesan en efectivo. Las políticas incluyen:\n\n" +
                            "• Pago anticipado para confirmar reservas\n" +
                            "• Reembolsos según política de cancelación\n" +
                            "• Cargos por cancelaciones tardías\n" +
                            "• No show: sin reembolso"
                )

                SeccionTerminos(
                    titulo = "7. Responsabilidades",
                    contenido = "Satori SPA actúa como intermediario entre clientes y terapeutas. No somos responsables de:\n\n" +
                            "• La calidad de los servicios proporcionados\n" +
                            "• Lesiones o daños durante el tratamiento\n" +
                            "• Cancelaciones por parte del terapeuta\n" +
                            "• Resultados específicos de los tratamientos"
                )

                SeccionTerminos(
                    titulo = "8. Propiedad Intelectual",
                    contenido = "Todo el contenido de la aplicación, incluyendo textos, gráficos, logos e imágenes, es propiedad de Satori SPA y está protegido por leyes de propiedad intelectual."
                )

                SeccionTerminos(
                    titulo = "9. Modificaciones",
                    contenido = "Nos reservamos el derecho de modificar estos términos en cualquier momento. Los cambios entrarán en vigencia inmediatamente después de su publicación en la aplicación."
                )

                SeccionTerminos(
                    titulo = "10. Terminación",
                    contenido = "Podemos suspender o terminar su acceso a la aplicación por:\n\n" +
                            "• Violación de estos términos\n" +
                            "• Comportamiento inapropiado\n" +
                            "• Fraude o actividades sospechosas\n" +
                            "• A nuestra discreción"
                )

                SeccionTerminos(
                    titulo = "11. Ley Aplicable",
                    contenido = "Estos términos se rigen por las leyes de México. Cualquier disputa será resuelta en los tribunales de Jalisco, México."
                )

                SeccionTerminos(
                    titulo = "12. Contacto",
                    contenido = "Si tiene preguntas sobre estos términos, puede contactarnos a:\n\n" +
                            "Email: soporte@satorispa.com\n" +
                            "Teléfono: +52 33 1234 5678\n" +
                            "Dirección: Manzanillo, Colima, México"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card de aceptación
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
                            Icons.Default.Description,
                            contentDescription = null,
                            tint = primaryBrandColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Al usar Satori SPA, aceptas estos términos y condiciones.",
                            fontSize = 13.sp,
                            color = textOnSecondaryPlatform,
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
fun SeccionTerminos(titulo: String, contenido: String) {
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