package com.narmocorp.satorispa.views.cliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.narmocorp.satorispa.model.Servicio
import com.narmocorp.satorispa.viewmodel.ServiciosViewModel

/**
 * Pantalla de servicios para clientes autenticados
 * Incluye el NavBar y muestra todos los servicios disponibles
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteServiciosScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    serviciosViewModel: ServiciosViewModel = viewModel(),
    onNavigateToNotifications: () -> Unit,
    onNavigateToConfig: () -> Unit
) {
    val services by serviciosViewModel.servicios.collectAsState()
    val categories = listOf("Todos") + services.map { it.categoriaNombre }.distinct()
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: "Todos") }
    var selectedService by remember { mutableStateOf<Servicio?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Colores del tema
    val primaryColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background
    val onSecondaryColor = MaterialTheme.colorScheme.onSecondary

    val filteredServices = services.filter { service ->
        (selectedCategory == "Todos" || service.categoriaNombre == selectedCategory) &&
                (searchQuery.isEmpty() || service.servicio.contains(searchQuery, ignoreCase = true))
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            // Se mantiene el TopBar original
            TopBar(
                onNavigateToNotifications = onNavigateToNotifications,
                onNavigateToConfig = onNavigateToConfig
            )
        },
        bottomBar = {
            NavBar(
                selectedRoute = "servicios",
                onHomeClick = { navController.navigate("cliente_home") },
                onServiciosClick = { navController.navigate("cliente_servicios") },
                onCitasClick = { navController.navigate("cliente_mis_citas") }
            )
        },
        containerColor = backgroundColor,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            // Título
            Text(
                text = "Servicios",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = primaryColor
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = primaryColor,
                    cursorColor = primaryColor,
                    focusedTextColor = onSecondaryColor,
                    unfocusedTextColor = onSecondaryColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filtros por categoría
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = category == selectedCategory,
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = primaryColor,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.surface,
                                labelColor = onSecondaryColor
                            )
                        )
                    }
                }
            }

            // Lista de servicios
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    // MODIFICACIÓN: Sección "Todos los servicios" con flecha (Alineado con Servicios.kt)
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Todos los servicios",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSecondaryColor
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowDownward,
                            contentDescription = "Scroll vertical",
                            tint = onSecondaryColor,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    // FIN MODIFICACIÓN

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Grid de servicios (2 columnas)
                items(filteredServices.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowItems.forEach { service ->
                            Box(modifier = Modifier.weight(1f)) {
                                ServiceCard(service = service) {
                                    selectedService = it
                                }
                            }
                        }
                        if (rowItems.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Modal de detalles del servicio
    selectedService?.let { service ->
        ServiceDetailsDialog(
            service = service,
            onDismiss = { selectedService = null },
            onBookClick = {
                // Reemplaza la lógica anterior:
                selectedService?.let { serviceToBook ->
                    navController.navigate("agendar_cita/${serviceToBook.id}")
                }
                selectedService = null
            }
        )
    }
}

@Composable
private fun ServiceCard(service: Servicio, onItemClick: (Servicio) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(service) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(service.imagen),
                contentDescription = service.servicio,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .height(100.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = service.servicio,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${service.precio}",
                        color = Color.Gray
                    )
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "Agendar cita",
                        tint = MaterialTheme.colorScheme.onSecondary // Alineado con Servicios.kt
                    )
                }
            }
        }
    }
}

@Composable
private fun ServiceDetailsDialog(
    service: Servicio,
    onDismiss: () -> Unit,
    onBookClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                service.servicio,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
        },
        text = {
            Column {
                Image(
                    painter = rememberAsyncImagePainter(service.imagen),
                    contentDescription = service.servicio,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Precio: $${service.precio}",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = service.descripcion,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onBookClick()
                onDismiss()
            }) {
                Text("Agendar Cita")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}
