package com.narmocorp.satorispa.views.terapeuta

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.narmocorp.satorispa.R
import com.narmocorp.satorispa.controller.CitasController
import com.narmocorp.satorispa.model.Cita
import java.text.SimpleDateFormat
import java.util.*

// Pantalla "Mis Citas" para el usuario terapeuta. Lee datos reales desde Firestore.
@Composable
fun TerapeutaCitasScreen(navController: NavController) {
    var citas by remember { mutableStateOf<List<Cita>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var selectedCita by remember { mutableStateOf<Cita?>(null) }
    var selectedDate by remember { mutableStateOf<String?>(null) }
    var displayedDate by remember { mutableStateOf<String?>(null) }

    // Escuchar citas en tiempo real y limpiar listener al recomponer/destruir
    DisposableEffect(Unit) {
        cargando = true
        errorMsg = null
        val registration = CitasController.escucharCitasTerapeutaRealtime(
            onUpdate = { lista: List<Cita> ->
                val ahora = Calendar.getInstance()
                val formatoFechaHora = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

                val citasActualizadas = lista.map { cita ->
                    if (cita.estado.equals("pendiente", ignoreCase = true)) {
                        try {
                            val fechaHoraCita = formatoFechaHora.parse("${cita.fecha} ${cita.hora}")
                            if (fechaHoraCita != null && ahora.time.after(fechaHoraCita)) {
                                CitasController.actualizarEstadoCita(cita.id, "confirmada")
                                cita.copy(estado = "confirmada")
                            } else {
                                cita
                            }
                        } catch (e: Exception) {
                            // En caso de error de parseo, mantener la cita original
                            cita
                        }
                    } else {
                        cita
                    }
                }
                citas = citasActualizadas
                cargando = false
            },
            onError = { mensaje: String ->
                errorMsg = mensaje
                cargando = false
            }
        )

        onDispose {
            registration?.remove()
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                onNavigateToNotifications = { /* implementar si hace falta */ },
                onNavigateToConfig = { navController.navigate("terapeuta_config") }
            )
        },
        bottomBar = {
            NavBar(
                selectedRoute = "citas",
                onHomeClick = { navController.navigate("terapeuta_home") },
                onCitasClick = { /* No hacer nada, ya estamos aquí */ }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        BoxWithConstraints(modifier = Modifier.padding(padding)) {
            val isTablet = maxWidth > 600.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Mis Citas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Filtro de fecha
                val context = LocalContext.current
                val calendar = Calendar.getInstance()
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        selectedDate =
                            "$year-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
                        val selectedCalendar = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth)
                        }
                        val dateFormat =
                            SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
                        displayedDate = dateFormat.format(selectedCalendar.time)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { datePickerDialog.show() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = "Seleccionar fecha",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            text = displayedDate ?: "Seleccionar fecha",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (selectedDate != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                selectedDate = null
                                displayedDate = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Limpiar")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val filteredCitas = if (selectedDate == null) {
                    citas
                } else {
                    citas.filter { it.fecha == selectedDate }
                }

                when {
                    cargando -> {
                        CircularProgressIndicator()
                    }
                    errorMsg != null -> {
                        Text(text = "Error: $errorMsg")
                    }
                    else -> {
                        Row(Modifier.fillMaxSize()) {
                            // Columna de la lista de citas
                            Box(modifier = Modifier.weight(if (isTablet) 1f else 1f)) {
                                if (filteredCitas.isEmpty()) {
                                    Text(
                                        text = if (selectedDate != null) "No hay citas para la fecha seleccionada"
                                        else "No hay citas asignadas",
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                } else {
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        items(filteredCitas) { cita ->
                                            CitaCard(cita, isSelected = cita.id == selectedCita?.id) {
                                                selectedCita = cita
                                            }
                                        }
                                    }
                                }
                            }

                            // Vista de detalles para tablets
                            if (isTablet) {
                                Box(
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .padding(start = 16.dp)
                                        .fillMaxHeight()
                                ) {
                                    if (selectedCita != null) {
                                        Surface(
                                            shape = RoundedCornerShape(16.dp),
                                            color = MaterialTheme.colorScheme.surface,
                                            tonalElevation = 2.dp,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            CitaDetailsContent(cita = selectedCita!!)
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("Selecciona una cita para ver los detalles.")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Modal para teléfonos
            if (!isTablet && selectedCita != null) {
                CitaDetailsModal(cita = selectedCita!!, onDismiss = { selectedCita = null })
            }
        }
    }
}

// Se añade el parámetro isSelected para resaltar la tarjeta seleccionada en modo tablet
@Composable
private fun CitaCard(cita: Cita, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 88.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!cita.imagenServicio.isNullOrEmpty()) {
                AsyncImage(
                    model = cita.imagenServicio,
                    contentDescription = cita.servicio,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.logo),
                    placeholder = painterResource(id = R.drawable.logo)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = cita.servicio,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = cita.cliente.ifEmpty { cita.servicio }, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${cita.fecha} ${cita.hora}",
                    maxLines = 2,
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

// Nuevo Composable extraído para mostrar el contenido de los detalles
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

        if (!cita.imagenServicio.isNullOrEmpty()) {
            AsyncImage(
                model = cita.imagenServicio,
                contentDescription = cita.servicio,
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.logo),
                error = painterResource(id = R.drawable.logo)
            )
        }

        DetailRow(icon = Icons.Filled.Spa, text = "Servicio: ${cita.servicio}")
        DetailRow(icon = Icons.Filled.Person, text = "Cliente: ${cita.cliente}")
        DetailRow(icon = Icons.Default.CalendarToday, text = "Fecha: ${cita.fecha}")
        DetailRow(icon = Icons.Filled.Schedule, text = "Hora: ${cita.hora}")

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.Label,
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
private fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
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
    val (backgroundColor, textColor) = when (status.lowercase()) {
        "confirmada" -> Color(0xFF4CAF50) to Color.White
        "pendiente" -> Color(0xFFFFB300) to Color.Black
        "cancelada" -> Color(0xFFEF5350) to Color.White
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
