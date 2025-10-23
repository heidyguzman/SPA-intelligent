package com.narmocorp.satorispa.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.narmocorp.satorispa.R
import com.narmocorp.satorispa.model.Servicio
import com.narmocorp.satorispa.viewmodel.ServiciosViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    navController: NavController,
    isGuest: Boolean = true,
    serviciosViewModel: ServiciosViewModel = viewModel()
) {
    val services by serviciosViewModel.servicios.collectAsState()
    val categories = listOf("Todos") + services.map { it.categoria }.distinct()
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: "Todos") }
    var selectedService by remember { mutableStateOf<Servicio?>(null) }

    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val filteredServices = services.filter { service ->
        (selectedCategory == "Todos" || service.categoria == selectedCategory) &&
                (searchQuery.isEmpty() || service.nombre.contains(searchQuery, ignoreCase = true))
    }

    val recentServices = services.sortedByDescending { it.createdAt }.take(4)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Satori Spa Logo",
                        modifier = Modifier
                            .height(50.dp)
                    )
                },
                actions = {
                    if (isGuest) {
                        IconButton(onClick = { navController.navigate("login") }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Login", modifier = Modifier.size(36.dp))
                        }
                    } else {
                        // TODO: Show Profile/Logout icon for logged-in user
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            Text(
                text = "Servicios",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(30.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = category == selectedCategory,
                        onClick = { selectedCategory = category },
                        label = { Text(category) }
                    )
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Lo más reciente",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(recentServices) { service ->
                            PopularServiceItem(service = service) {
                                selectedService = it
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Todos los servicios",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(filteredServices.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowItems.forEach { service ->
                            Box(modifier = Modifier.weight(1f)) {
                                ServiceItem(service = service) {
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

    selectedService?.let { service ->
        ServiceDetailsModal(
            service = service,
            onDismiss = { selectedService = null },
            onBookClick = {
                if (isGuest) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Inicia sesión para agendar una cita",
                            duration = SnackbarDuration.Short
                        )
                    }
                } else {
                    // TODO: Handle booking for logged-in user
                }
            }
        )
    }
}

@Composable
fun PopularServiceItem(service: Servicio, onItemClick: (Servicio) -> Unit) {
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
                    fontSize = 14.sp
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
                    Icon(Icons.Default.CalendarToday, contentDescription = "Agendar cita")
                }
            }
        }
    }
}

@Composable
fun ServiceItem(service: Servicio, onItemClick: (Servicio) -> Unit) {
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
                Text(text = service.nombre, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "$${service.precio}", color = Color.Gray)
                    Icon(Icons.Default.CalendarToday, contentDescription = "Agendar cita")
                }
            }
        }
    }
}

@Composable
fun ServiceDetailsModal(service: Servicio, onDismiss: () -> Unit, onBookClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(service.nombre, fontWeight = FontWeight.Bold, fontSize = 24.sp) },
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
                Text(text = "Precio: $${service.precio}", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = service.descripcion, fontSize = 16.sp, color = Color.Gray)

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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ServicesScreenPreview() {
    ServicesScreen(navController = rememberNavController(), isGuest = true)
}
