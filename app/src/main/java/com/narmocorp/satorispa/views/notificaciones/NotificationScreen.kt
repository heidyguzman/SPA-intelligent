package com.narmocorp.satorispa.views.notificaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    onNotificationClick: ((NotificationData) -> Unit)? = null  // ← AGREGAR ESTE PARÁMETRO
) {
    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedNotifications by remember { mutableStateOf(setOf<Int>()) }

    val notifications = remember {
        listOf(
            NotificationData(
                1, "Nueva cita programada",
                "Su cita ha sido confirmada para mañana",
                "3:58 PM", isNew = true, newCount = 2, section = "Esta semana"
            ),
            NotificationData(
                2, "Recordatorio de cita",
                "No olvide su cita de hoy a las 3:00 PM",
                "3:58 PM", isNew = true, newCount = 1, section = "Esta semana"
            ),
            NotificationData(
                3, "Promoción especial",
                "Disfrute 20% de descuento en tratamientos faciales",
                "22/sept/25", isNew = false, section = "Anteriores"
            ),
            NotificationData(
                4, "Bienvenido al spa",
                "Gracias por elegir nuestros servicios",
                "14/sept/25", isNew = false, section = "Anteriores"
            ),
        )
    }

    val thisWeekNotifications = notifications.filter { it.section == "Esta semana" }
    val previousNotifications = notifications.filter { it.section == "Anteriores" }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header ovalado
            NotificationHeader(
                isSelectionMode = isSelectionMode,
                onBackClick = {
                    if (isSelectionMode) {
                        isSelectionMode = false
                        selectedNotifications = setOf()
                    } else {
                        onBackClick?.invoke()
                    }
                }
            )

            // Lista de notificaciones
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = if (isSelectionMode) 80.dp else 16.dp)
            ) {
                // Sección "Esta semana"
                item {
                    SectionHeader(
                        title = "Esta semana",
                        actionText = if (isSelectionMode) "Seleccionar todo" else "Seleccionar",
                        onActionClick = {
                            if (isSelectionMode) {
                                // Seleccionar TODAS las notificaciones (ambas secciones)
                                selectedNotifications = notifications.map { it.id }.toSet()
                            } else {
                                isSelectionMode = true
                            }
                        }
                    )
                }

                items(thisWeekNotifications) { notification ->
                    NotificationCard(
                        notification = notification,
                        isSelected = selectedNotifications.contains(notification.id),
                        isSelectionMode = isSelectionMode,
                        onCardClick = {
                            if (isSelectionMode) {
                                selectedNotifications = if (selectedNotifications.contains(notification.id)) {
                                    selectedNotifications - notification.id
                                } else {
                                    selectedNotifications + notification.id
                                }
                            } else {
                                // Abrir detalle cuando NO está en modo selección
                                onNotificationClick?.invoke(notification)
                            }
                        }
                    )
                }

                // Sección "Anteriores"
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    SectionHeader(
                        title = "Anteriores",
                        actionText = "", // Sin texto de acción
                        onActionClick = { }
                    )
                }

                items(previousNotifications) { notification ->
                    NotificationCard(
                        notification = notification,
                        isSelected = selectedNotifications.contains(notification.id),
                        isSelectionMode = isSelectionMode,
                        onCardClick = {
                            if (isSelectionMode) {
                                selectedNotifications = if (selectedNotifications.contains(notification.id)) {
                                    selectedNotifications - notification.id
                                } else {
                                    selectedNotifications + notification.id
                                }
                            } else {  // ← AGREGAR ESTE ELSE
                                // Abrir detalle cuando NO está en modo selección
                                onNotificationClick?.invoke(notification)
                            }
                        }
                    )
                }
            }
        }

        // Botones de acción (Cancelar / Eliminar)
        if (isSelectionMode) {
            ActionButtons(
                selectedCount = selectedNotifications.size,
                onCancel = {
                    isSelectionMode = false
                    selectedNotifications = setOf()
                },
                onDelete = {
                    println("Eliminar notificaciones: $selectedNotifications")
                    isSelectionMode = false
                    selectedNotifications = setOf()
                }
            )
        }
    }
}

@Composable
private fun NotificationHeader(
    isSelectionMode: Boolean,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(AppColors.primaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = AppColors.onSurface
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Notificaciones",
                color = AppColors.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun BoxScope.ActionButtons(
    selectedCount: Int,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Red
            )
        ) {
            Text("Cancelar", fontSize = 16.sp)
        }

        Button(
            onClick = onDelete,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            ),
            enabled = selectedCount > 0
        ) {
            Text("Eliminar", fontSize = 16.sp)
        }
    }
}