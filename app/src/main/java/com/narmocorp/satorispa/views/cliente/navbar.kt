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
    onCitasClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = Color(0xffdfbaa2)
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (selectedRoute == "inicio") Color(0xff995d2d) else Color(0xff995d2d).copy(alpha = 0.6f)
                )
            },
            label = {
                Text(
                    "Home",
                    color = if (selectedRoute == "inicio") Color(0xff995d2d) else Color(0xff995d2d).copy(alpha = 0.6f)
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
                    tint = if (selectedRoute == "servicios") Color(0xff995d2d) else Color(0xff995d2d).copy(alpha = 0.6f)
                )
            },
            label = {
                Text(
                    "Servicios",
                    color = if (selectedRoute == "servicios") Color(0xff995d2d) else Color(0xff995d2d).copy(alpha = 0.6f)
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
                    tint = if (selectedRoute == "citas") Color(0xff976826) else Color(0xff976826).copy(alpha = 0.6f)
                )
            },
            label = {
                Text(
                    "Mis citas",
                    color = if (selectedRoute == "citas") Color(0xff976826) else Color(0xff976826).copy(alpha = 0.6f)
                )
            },
            selected = selectedRoute == "citas",
            onClick = onCitasClick
        )
    }
}