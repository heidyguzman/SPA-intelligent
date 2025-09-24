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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(Modifier.weight(1f))
                        // Cambiado Icon por Image para el logo
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "logo",
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(Modifier.weight(1f))
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Notificaciones */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones"
                        )
                    }
                    IconButton(onClick = { /* TODO: Ajustes */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ajustes"
                        )
                    }
                }
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
                    label = { Text("Home") },
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
                    label = { Text("Servicios") },
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
                    label = { Text("Mis citas") },
                    selected = false,
                    onClick = { /* TODO: Mis citas */ }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
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
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff1c1b1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = usuario?.apellido ?: "Apellido",
                fontSize = 24.sp,
                color = Color(0xff1c1b1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = usuario?.correo ?: "Correo",
                fontSize = 18.sp,
                color = Color(0xff1c1b1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(32.dp))
            // Card NFC
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xff995d2d).copy(alpha = 0.79f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(Color(0xffdbbba6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo), // Cambiado contactless por logo temporalmente
                            contentDescription = "contactless",
                            modifier = Modifier.size(39.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Acerque el dispositivo",
                        fontSize = 16.sp,
                        color = Color(0xff1c1b1f)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
