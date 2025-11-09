package com.narmocorp.satorispa.views.cliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.narmocorp.satorispa.model.Cita
import com.narmocorp.satorispa.viewmodel.MisCitasViewModel
import java.text.SimpleDateFormat
import java.util.Locale

// Colores de estado
val StatusColorSuccess = Color(0xFF4CAF50)
val StatusColorError = Color(0xFFEF5350)
val StatusColorPending = Color(0xFFFFB300)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(
    navController: NavController,
    viewModel: MisCitasViewModel = viewModel(),
    onNavigateToNotifications: () -> Unit,
    onNavigateToConfig: () -> Unit
) {
    val citas by viewModel.citas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var selectedCita by remember { mutableStateOf<Cita?>(null) }

    Scaffold(
        topBar = {
            TopBar(
                onNavigateToNotifications = onNavigateToNotifications,
                onNavigateToConfig = onNavigateToConfig
            )
        },
        bottomBar = {
            NavBar(
                selectedRoute = "citas",
                onHomeClick = { navController.navigate("cliente_home") },
                onServiciosClick = { navController.navigate("cliente_servicios") },
                onCitasClick = { navController.navigate("cliente_mis_citas") }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(
                text = "Mis Citas",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(16.dp))


            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                citas.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "No tienes citas agendadas aún. ¡Reserva una!",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(citas) { cita ->
                            CitaCard(cita = cita) {
                                selectedCita = cita
                            }
                        }
                    }
                }
            }
        }
    }

    selectedCita?.let { cita ->
        CitaDetailsModal(cita = cita, onDismiss = { selectedCita = null })
    }
}

// -------------------------------------------------------------------------
// COMPONENTES DE DISEÑO
// -------------------------------------------------------------------------

@Composable
fun CitaCard(cita: Cita, onClick: () -> Unit) {
    // ...
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            // 💡 CAMBIO SOLICITADO: Color de fondo más bajo
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = cita.servicioImagen),
                contentDescription = cita.servicioNombre,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = cita.servicioNombre, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${formatDateUi(cita.fecha)} ${cita.hora}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            StatusIndicator(status = cita.estado)
        }
    }
}

@Composable
fun CitaDetailsModal(cita: Cita, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CitaDetailsContent(cita = cita)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}

@Composable
private fun CitaDetailsContent(cita: Cita) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Detalles de la Cita",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        if (cita.servicioImagen.isNotBlank()) {
            Image(
                painter = rememberAsyncImagePainter(model = cita.servicioImagen),
                contentDescription = cita.servicio,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )
        }

        DetailRow(icon = Icons.Filled.Spa, text = "Servicio: ${cita.servicioNombre}")
        DetailRow(icon = Icons.Default.Phone, text = "Teléfono: ${cita.telefono}")
        DetailRow(icon = Icons.Default.CalendarToday, text = "Fecha: ${formatDateUi(cita.fecha)}")
        DetailRow(icon = Icons.Filled.Schedule, text = "Hora: ${cita.hora}")

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                // 💡 CORREGIDO: Usar AutoMirrored.Filled.Label
                Icons.AutoMirrored.Filled.Label,
                contentDescription = "Estado",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Estado: ")
            Spacer(modifier = Modifier.width(4.dp))
            StatusIndicator(status = cita.estado)
        }
    }
}

@Composable
private fun DetailRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Composable
private fun StatusIndicator(status: String) {
    // 💡 CORRECCIÓN APLICADA: Color de texto de Pendiente ahora es White
    val (backgroundColor, textColor) = when (status.lowercase(Locale.ROOT)) {
        "confirmada" -> StatusColorSuccess to Color.White
        "pendiente" -> StatusColorPending to Color.White // ¡AQUÍ ESTÁ EL CAMBIO!
        "cancelada" -> StatusColorError to Color.White
        else -> MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.onSurface
    }

    Surface(
        shape = RoundedCornerShape(50.dp),
        color = backgroundColor,
        contentColor = textColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = status,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = textColor
            )
        }
    }
}

fun formatDateUi(dateString: String): String {
    val spanishLocale = Locale.forLanguageTag("es-ES")

    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", spanishLocale)
        val date = inputFormat.parse(dateString)

        if (date != null) {
            outputFormat.format(date).replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(spanishLocale) else it.toString()
            }
        } else {
            dateString
        }
    } catch (_: Exception) {
        dateString
    }
}