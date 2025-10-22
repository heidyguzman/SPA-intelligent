package com.narmocorp.satorispa.views.cliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.narmocorp.satorispa.viewmodel.ClientHomeViewModel
import com.narmocorp.satorispa.viewmodel.UserState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: ClientHomeViewModel = viewModel(),
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToConfig: () -> Unit = {},
    selectedRoute: String = "inicio",
    onHomeClick: () -> Unit = {},
    onServiciosClick: () -> Unit = {},
    onCitasClick: () -> Unit = {}
) {
    val userState by viewModel.userState.collectAsState()

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
                onServiciosClick = onServiciosClick,
                onCitasClick = onCitasClick
            )
        },
        containerColor = Color.White,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Avatar con soporte para imagen
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xffdbbba6)),
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
                                tint = Color(0xff1c1b1f),
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }
                    else -> {
                        // Mientras carga o hay error, mostrar icono por defecto
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Perfil",
                            tint = Color(0xff1c1b1f),
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
                        color = Color(0xff995d2d)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Cargando...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xff1c1b1f).copy(alpha = 0.7f)
                    )
                }

                is UserState.Success -> {
                    val user = (userState as UserState.Success).user
                    Text(
                        text = user.nombre,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xff1c1b1f)
                    )
                    Text(
                        text = user.apellido,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xff1c1b1f)
                    )
                    Text(
                        text = user.correo,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xff1c1b1f).copy(alpha = 0.7f)
                    )
                }

                is UserState.Error -> {
                    val errorMessage = (userState as UserState.Error).message
                    Text(
                        text = "Error al cargar datos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.loadUserData() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff995d2d)
                        )
                    ) {
                        Text("Reintentar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))

            // Card NFC
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xff995d2d)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Registra tu entrada",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xffdbbba6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Nfc,
                            contentDescription = "NFC Icon",
                            modifier = Modifier.size(42.dp),
                            tint = Color(0xff1c1b1f)
                        )
                    }
                    Text(
                        text = "Acerque su dispositivo al lector para realizar el registro de entrada",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientHomeScreenPreview() {
    ClientHomeScreen()
}