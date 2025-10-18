package com.narmocorp.satorispa.views.terapeuta

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
import androidx.compose.material.icons.filled.RoomService
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narmocorp.satorispa.R
import com.narmocorp.satorispa.views.cliente.NavBar
import com.narmocorp.satorispa.views.cliente.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerapeutaHomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToConfig: () -> Unit = {},
    selectedRoute: String = "",
    onHomeClick: () -> Unit = {},
    onServiciosClick: () -> Unit = {},
    onCitasClick: () -> Unit = {}
) {
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
                text = "Nombre",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xff1c1b1f)
            )
            Text(
                text = "Apellido",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xff1c1b1f)
            )
            Text(
                text = "Correo",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xff1c1b1f).copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(100.dp)) // Espacio aumentado para bajar el cuadro NFC

        }
    }
}

@Preview(showBackground = true)
@Composable
fun TerapeutaHomeScreenPreview() {
    TerapeutaHomeScreen()
}