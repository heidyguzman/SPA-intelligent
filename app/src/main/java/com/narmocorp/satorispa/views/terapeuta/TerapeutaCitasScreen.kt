package com.narmocorp.satorispa.views.terapeuta

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.narmocorp.satorispa.R
import com.narmocorp.satorispa.controller.CitasController
import com.narmocorp.satorispa.model.Cita

// Pantalla "Mis Citas" para el usuario terapeuta. Lee datos reales desde Firestore.
@Composable
fun TerapeutaCitasScreen(navController: NavController) {
    var citas by remember { mutableStateOf<List<Cita>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    // Escuchar citas en tiempo real y limpiar listener al recomponer/destruir
    DisposableEffect(Unit) {
        cargando = true
        errorMsg = null
        val registration = CitasController.escucharCitasTerapeutaRealtime(
            onUpdate = { lista: List<Cita> ->
                citas = lista
                cargando = false
            },
            onError = { mensaje: String ->
                errorMsg = mensaje
                cargando = false
            }
        )

        onDispose {
            registration?.remove()
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                onNavigateToNotifications = { /* implementar si hace falta */ },
                onNavigateToConfig = { navController.navigate("terapeuta_config") }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mis Citas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                cargando -> {
                    CircularProgressIndicator()
                }
                errorMsg != null -> {
                    Text(text = "Error: ${errorMsg}")
                }
                citas.isEmpty() -> {
                    Text(text = "No hay citas asignadas")
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(citas) { cita ->
                            CitaCard(cita)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CitaCard(cita: Cita) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 88.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = cita.servicio,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = cita.cliente.ifEmpty { cita.servicio }, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${cita.fecha} ${cita.hora}", maxLines = 2, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = cita.estado, modifier = Modifier.padding(start = 8.dp))
        }
    }
}
