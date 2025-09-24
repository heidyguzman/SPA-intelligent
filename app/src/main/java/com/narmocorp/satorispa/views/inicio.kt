package com.narmocorp.satorispa.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Build // Alternativa a RoomService
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narmocorp.satorispa.R
import com.narmocorp.satorispa.models.Usuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Inicio(
    modifier: Modifier = Modifier,
    usuario: Usuario? = null // Nuevo parámetro
    , onNavigateToNotifications: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "logo",
                            modifier = Modifier.size(60.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateToNotifications?.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = Color(0xff995d2d)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Ajustes */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ajustes",
                            tint = Color(0xff995d2d)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xffdfbaa2)
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color(0xff995d2d)
                        )
                    },
                    label = { Text("Home", color = Color(0xff995d2d)) },
                    selected = true,
                    onClick = { /* TODO: Home */ }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Build, // Usar Build como alternativo a RoomService
                            contentDescription = "Servicios",
                            tint = Color(0xff995d2d)
                        )
                    },
                    label = { Text("Servicios", color = Color(0xff995d2d)) },
                    selected = false,
                    onClick = { /* TODO: Servicios */ }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.DateRange, // Usar DateRange como alternativo a Event
                            contentDescription = "Mis citas",
                            tint = Color(0xff976826)
                        )
                    },
                    label = { Text("Mis citas", color = Color(0xff976826)) },
                    selected = false,
                    onClick = { /* TODO: Mis citas */ }
                )
            }
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
            // Avatar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xffdbbba6)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = Color(0xff1c1b1f),
                    modifier = Modifier.size(100.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = usuario?.nombre ?: "Nombre",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xff1c1b1f)
            )
            Text(
                text = usuario?.apellido ?: "Apellido",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xff1c1b1f)
            )
            Text(
                text = usuario?.correo ?: "Correo",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xff1c1b1f).copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(32.dp))
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
                        text = "Paga con tu móvil",
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
                        text = "Acerque su dispositivo al lector para realizar el pago",
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