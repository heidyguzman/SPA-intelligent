
package com.narmocorp.satorispa.views.cliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.narmocorp.satorispa.R
import com.narmocorp.satorispa.views.IconoCampanaConBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onNavigateToNotifications: (() -> Unit)? = null,
    onNavigateToConfig: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier.size(80.dp)
                )
            }
        },
        navigationIcon = {
            IconoCampanaConBadge(
                onClick = { onNavigateToNotifications?.invoke() },
                tint = MaterialTheme.colorScheme.primary // Usa el color primary del tema
            )
        },
        actions = {
            IconButton(onClick = { onNavigateToConfig?.invoke() }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Ajustes",
                    tint = MaterialTheme.colorScheme.primary // Usa el color primary del tema
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background // Usa el color de fondo del tema
        )
    )
}
