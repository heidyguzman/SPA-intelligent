package com.narmocorp.satorispa.views

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun NavBar(
    onHomeClick: (() -> Unit)? = null,
    onServiciosClick: (() -> Unit)? = null,
    onCitasClick: (() -> Unit)? = null
) {
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
            onClick = { onHomeClick?.invoke() }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.RoomService,
                    contentDescription = "Servicios",
                    tint = Color(0xff995d2d)
                )
            },
            label = { Text("Servicios", color = Color(0xff995d2d)) },
            selected = false,
            onClick = { onServiciosClick?.invoke() }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Citas",
                    tint = Color(0xff976826)
                )
            },
            label = { Text("Mis citas", color = Color(0xff976826)) },
            selected = false,
            onClick = { onCitasClick?.invoke() }
        )
    }
}
