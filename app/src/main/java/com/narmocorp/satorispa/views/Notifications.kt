package com.narmocorp.satorispa.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narmocorp.satorispa.R

// Colores del tema
object AppColors {
    val color_Light_Theme_On_Surface_20 = Color(0xFF1C1B1F)
    val color_Light_Theme_On_Surface_60 = Color(0xFF49454F)
    val primary = Color(0xFF995D2D)
    val primaryContainer = Color(0xFFDBBBA6)
    val surface = Color(0xFFF8F9FA)
    val onSurface = Color(0xFF1C1B1F)
}

data class NotificationData(
    val title: String,
    val description: String,
    val time: String,
    val isNew: Boolean = false,
    val newCount: Int = 0
)

class Notifications {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Notificaciones(
        modifier: Modifier = Modifier,
        onBackClick: (() -> Unit)? = null
    ) {
        val notifications = listOf(
            NotificationData(
                "Nueva cita programada",
                "Su cita ha sido confirmada para mañana",
                "3:58 PM",
                isNew = false
            ),
            NotificationData(
                "Recordatorio de cita",
                "No olvide su cita de hoy a las 3:00 PM",
                "3:58 PM",
                isNew = false
            ),
            NotificationData(
                "Promoción especial",
                "Disfrute 20% de descuento en tratamientos faciales",
                "3:58 PM",
                isNew = true,
                newCount = 2
            ),
            NotificationData(
                "Bienvenido al spa",
                "Gracias por elegir nuestros servicios",
                "3:58 PM",
                isNew = true,
                newCount = 1
            )
        )

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(AppColors.surface)
        ) {
            // Header ovalado personalizado
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 32.dp) // Más espacio arriba
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFFD8B49C)) // Color más claro, similar al de la imagen
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón circular separado
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AppColors.primary)
                            .clickable { onBackClick?.invoke() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.Black // Ícono negro
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Notificaciones", // Texto cambiado
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold // Negrita
                    )
                }
            }

            // Lista de notificaciones
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 88.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(notifications) { notification ->
                    NotificationCard(
                        title = notification.title,
                        description = notification.description,
                        time = notification.time,
                        isNew = notification.isNew,
                        newCount = notification.newCount
                    )
                }
            }
        }
    }

    @Composable
    private fun NotificationCard(
        title: String,
        description: String,
        time: String,
        isNew: Boolean = false,
        newCount: Int = 0
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Acción al hacer clic */ },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono del spa
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(AppColors.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Icono spa",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                    )
                }

                // Contenido de la notificación
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        color = AppColors.color_Light_Theme_On_Surface_20,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = description,
                        color = AppColors.color_Light_Theme_On_Surface_60,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Hora y punto de notificación nueva
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = time,
                        color = AppColors.color_Light_Theme_On_Surface_60,
                        fontSize = 12.sp
                    )

                    // Punto azul para notificaciones nuevas
                    if (isNew && newCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2196F3)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = newCount.toString(),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun NotificacionesPreview() {
        MaterialTheme {
            Notificaciones()
        }
    }
}