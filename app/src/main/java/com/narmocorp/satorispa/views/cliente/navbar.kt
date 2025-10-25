package com.narmocorp.satorispa.views.cliente

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun NavBar(
    selectedRoute: String,
    onHomeClick: () -> Unit = {},
    onServiciosClick: () -> Unit = {},
    onCitasClick: () -> Unit = {},
) {
    val brandPrimary = MaterialTheme.colorScheme.primary
    val brandSecondary = MaterialTheme.colorScheme.secondary
    val selectedContentColor = MaterialTheme.colorScheme.onSecondary

    NavigationBar(
        containerColor = brandSecondary
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (selectedRoute == "inicio") brandPrimary else brandPrimary.copy(alpha = 0.6f)
                )
            },
            label = {
                Text(
                    "Home",
                    color = if (selectedRoute == "inicio") selectedContentColor else selectedContentColor.copy(alpha = 0.6f)
                )
            },
            selected = selectedRoute == "inicio",

            onClick = onHomeClick
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.RoomService,
                    contentDescription = "Servicios",
                    tint = if (selectedRoute == "servicios") brandPrimary else brandPrimary.copy(alpha = 0.6f)
                )
            },
            label = {
                Text(
                    "Servicios",
                    color = if (selectedRoute == "servicios") selectedContentColor else selectedContentColor.copy(alpha = 0.6f)
                )
            },
            selected = selectedRoute == "servicios",
            onClick = onServiciosClick
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Citas",
                    tint = if (selectedRoute == "citas") brandPrimary else brandPrimary.copy(alpha = 0.6f)
                )
            },
            label = {
                Text(
                    "Mis citas",
                    color = if (selectedRoute == "citas") selectedContentColor else selectedContentColor.copy(alpha = 0.6f)
                )
            },
            selected = selectedRoute == "citas",
            onClick = onCitasClick
        )
    }
}