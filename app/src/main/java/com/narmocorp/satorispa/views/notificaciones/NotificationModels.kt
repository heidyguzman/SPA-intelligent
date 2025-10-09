package com.narmocorp.satorispa.views.notificaciones

import androidx.compose.ui.graphics.Color

// Colores del tema
object AppColors {
    val color_Light_Theme_On_Surface_20 = Color(0xFF1C1B1F)
    val color_Light_Theme_On_Surface_60 = Color(0xFF49454F)
    val primary = Color(0xFF995D2D)
    val primaryContainer = Color(0xFFDBBBA6)
    val surface = Color(0xFFF8F9FA)
    val onSurface = Color(0xFF1C1B1F)
}

data class NotificationData(
    val id: Int,
    val title: String,
    val description: String,
    val time: String,
    val isNew: Boolean = false,
    val newCount: Int = 0,
    val section: String // "Esta semana" o "Anteriores"
)