package com.narmocorp.satorispa.views.terapeuta

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.narmocorp.satorispa.R
import com.narmocorp.satorispa.controller.CitasController
import com.narmocorp.satorispa.model.Cita
import com.narmocorp.satorispa.viewmodel.TerapeutaHomeViewModel
import com.narmocorp.satorispa.viewmodel.UserState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerapeutaHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: TerapeutaHomeViewModel = viewModel(),
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToConfig: () -> Unit = {},
    selectedRoute: String = "inicio",
    onHomeClick: () -> Unit = {},
    onCitasClick: () -> Unit = {},
) {
    val userState by viewModel.userState.collectAsState()
    var hoyCitas by remember { mutableStateOf<List<Cita>>(emptyList()) }
    var cargandoCitas by remember { mutableStateOf(true) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshUserDataSilently()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        val citasRegistration = CitasController.escucharCitasDeHoyRealtime(
            onUpdate = {
                hoyCitas = it
                cargandoCitas = false
            },
            onError = { /* TODO: handle error */
                cargandoCitas = false
            }
        )

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            citasRegistration?.remove()
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                onNavigateToNotifications = onNavigateToNotifications,
                onNavigateToConfig = onNavigateToConfig
            )
        },
        bottomBar = {
            NavBar(
                selectedRoute = selectedRoute,
                onHomeClick = onHomeClick,
                onCitasClick = onCitasClick,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))

                // Avatar con soporte para imagen
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    when (userState) {
                        is UserState.Success -> {
                            val user = (userState as UserState.Success).user

                            // Si el usuario tiene imagen, mostrarla
                            if (user.imagenUrl.isNotEmpty()) {
                                Image(
                                    painter = rememberAsyncImagePainter(user.imagenUrl),
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                // Si no tiene imagen, mostrar icono por defecto
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Perfil",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(100.dp)
                                )
                            }
                        }
                        else -> {
                            // Mientras carga o hay error, mostrar icono por defecto
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Perfil",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Contenido dinámico según el estado
                when (userState) {
                    is UserState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Cargando...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    is UserState.Success -> {
                        val user = (userState as UserState.Success).user
                        Text(
                            text = user.nombre,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = user.apellido,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = user.correo,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    is UserState.Error -> {
                        val errorMessage = (userState as UserState.Error).message
                        Text(
                            text = "Error al cargar datos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.loadUserData() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Reintentar")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Sección de citas de hoy
                Text(
                    text = "Citas para hoy",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary

                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (cargandoCitas) {
                item {
                    CircularProgressIndicator()
                }
            } else if (hoyCitas.isEmpty()) {
                item {
                    Text(text = "No tienes citas para hoy")
                }
            } else {
                items(hoyCitas) { cita ->
                    CitaCardHome(cita) { /* TODO: handle click */ }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}


@Composable
private fun CitaCardHome(cita: Cita, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
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
                Text(text = "${cita.fecha} ${formatHora(cita.hora)}", maxLines = 2, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.width(8.dp))

            StatusIndicatorHome(status = cita.estado)
        }
    }
}


@Composable
private fun StatusIndicatorHome(status: String) {
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

private fun formatHora(hora24: String): String {
    return try {
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = inputFormat.parse(hora24)
        if (date != null) outputFormat.format(date) else hora24
    } catch (e: Exception) {
        hora24 // Devuelve la hora original si hay un error
    }
}

@Preview(showBackground = true)
@Composable
fun TerapeutaHomeScreenPreview() {
    TerapeutaHomeScreen()
}
