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
import kotlinx.coroutines.launch

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
    val categories = listOf("Todos") + services.map { it.categoria }.distinct()
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
        (selectedCategory == "Todos" || service.categoria == selectedCategory) &&
                (searchQuery.isEmpty() || service.nombre.contains(searchQuery, ignoreCase = true))
    }

    val popularServices = services.shuffled().take(4)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            // Error corregido: Las funciones ya están en el ámbito
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
                onCitasClick = { /* TODO: navegar a citas */ }
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

                    // Sección "Lo más popular"
                    Text(
                        text = "Lo más popular",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSecondaryColor,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(popularServices) { service ->
                            PopularServiceCard(service = service) {
                                selectedService = it
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sección "Todos los servicios"
                    Text(
                        text = "Todos los servicios",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSecondaryColor,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

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
                selectedService = null
                // TODO: Navegar a agendar cita con el servicio seleccionado
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Funcionalidad de agendar cita próximamente",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        )
    }
}

@Composable
private fun PopularServiceCard(service: Servicio, onItemClick: (Servicio) -> Unit) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val onSecondaryColor = MaterialTheme.colorScheme.onSecondary

    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onItemClick(service) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(service.imagen),
                contentDescription = service.nombre,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .height(80.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = service.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = onSecondaryColor
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${service.precio}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "Agendar cita",
                        tint = primaryColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ServiceCard(service: Servicio, onItemClick: (Servicio) -> Unit) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val onSecondaryColor = MaterialTheme.colorScheme.onSecondary

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
                contentDescription = service.nombre,
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
                    text = service.nombre,
                    fontWeight = FontWeight.Bold,
                    color = onSecondaryColor
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
                        tint = primaryColor
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
    val primaryColor = MaterialTheme.colorScheme.primary
    val onSecondaryColor = MaterialTheme.colorScheme.onSecondary

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                service.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = onSecondaryColor
            )
        },
        text = {
            Column {
                Image(
                    painter = rememberAsyncImagePainter(service.imagen),
                    contentDescription = service.nombre,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Precio: $${service.precio}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = onSecondaryColor
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
            Button(
                onClick = onBookClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Agendar Cita")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = primaryColor
                )
            ) {
                Text("Cerrar")
            }
        }
    )
}