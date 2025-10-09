package com.narmocorp.satorispa.views
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.narmocorp.satorispa.views.notificaciones.NotificationsScreen

class Notifications {
    @Composable
    fun Notificaciones(
        modifier: Modifier = Modifier,
        onBackClick: (() -> Unit)? = null
    ) {
        NotificationsScreen(
            modifier = modifier,
            onBackClick = onBackClick
        )
    }
}