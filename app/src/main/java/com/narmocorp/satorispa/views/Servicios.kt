package com.narmocorp.satorispa.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Login
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
import com.narmocorp.satorispa.R
import kotlinx.coroutines.launch

data class Service(val name: String, val price: String, val imageRes: Int, val category: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(isGuest: Boolean = true) {
    val services = listOf(
        Service("Masaje relajante", "$150.00", R.drawable.fondo, "Masajes"),
        Service("Masaje con piedras calientes", "$180.00", R.drawable.fondo, "Masajes"),
        Service("Masaje aromaterapéutico", "$200.00", R.drawable.fondo, "Masajes"),
        Service("Masaje de tejido profundo", "$240.00", R.drawable.fondo, "Masajes"),
        Service("Limpieza facial profunda", "$120.00", R.drawable.fondo, "Faciales"),
        Service("Tratamiento hidratante", "$180.00", R.drawable.fondo, "Faciales")
    )
    val categories = listOf("Todos") + services.map { it.category }.distinct()
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val filteredServices = services.filter { service ->
        (selectedCategory == "Todos" || service.category == selectedCategory) &&
                (searchQuery.isEmpty() || service.name.contains(searchQuery, ignoreCase = true))
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Satori Spa Logo",
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth()
                    )
                },
                actions = {
                    if (isGuest) {
                        IconButton(onClick = { /* TODO: Handle login navigation */ }) {
                            Icon(Icons.Default.Login, contentDescription = "Login")
                        }
                    } else {
                        // TODO: Show Profile/Logout icon for logged-in user
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
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

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredServices) { service ->
                    ServiceItem(service = service) {
                        if (isGuest) {
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Inicia sesión para agendar una cita",
                                    actionLabel = "Login"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    // TODO: Navigate to Login Screen
                                }
                            }
                        } else {
                            // TODO: Handle booking for logged-in user
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceItem(service: Service, onBookClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = service.imageRes),
                contentDescription = service.name,
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
                Text(text = service.name, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = service.price, color = Color.Gray)
                    IconButton(onClick = onBookClick) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Agendar cita")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ServicesScreenPreview() {
    ServicesScreen(isGuest = true)
}
