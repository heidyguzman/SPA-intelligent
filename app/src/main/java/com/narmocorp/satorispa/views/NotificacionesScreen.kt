package com.narmocorp.satorispa.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.narmocorp.satorispa.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.filled.Notifications

data class Notificacion(
    val id: String = "",
    val titulo: String = "",
    val mensaje: String = "",
    val tipo: String = "",
    val fecha: Long = 0,
    val leida: Boolean = false
)

@Composable
fun NotificacionesScreen(navController: NavController) {
    var notificaciones by remember { mutableStateOf<List<Notificacion>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // Colores del tema
    val primaryBrandColor = MaterialTheme.colorScheme.primary
    val secondaryBrandColor = MaterialTheme.colorScheme.secondary
    val textOnSecondaryPlatform = MaterialTheme.colorScheme.onSecondary
    val backgroundColor = MaterialTheme.colorScheme.background

    // Cargar notificaciones al iniciar
    LaunchedEffect(Unit) {
        scope.launch {
            cargarNotificaciones { listaNotificaciones ->
                notificaciones = listaNotificaciones
                cargando = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header con esquinas redondeadas (tipo card compacto)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(secondaryBrandColor)
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = textOnSecondaryPlatform,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Notificaciones",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textOnSecondaryPlatform
                )
            }

            if (cargando) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryBrandColor)
                }
            } else if (notificaciones.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.LightGray
                        )
                        Text(
                            "No hay notificaciones",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                val notificacionesAgrupadas = agruparPorSemana(notificaciones)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Esta semana
                    if (notificacionesAgrupadas["estaSemana"]?.isNotEmpty() == true) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Esta semana",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textOnSecondaryPlatform
                                )
                            }
                        }

                        items(notificacionesAgrupadas["estaSemana"] ?: emptyList()) { notif ->
                            TarjetaNotificacion(
                                notif,
                                onMarcarLeida = {
                                    scope.launch {
                                        marcarComoLeida(notif.id)
                                        cargarNotificaciones { listaNotificaciones ->
                                            notificaciones = listaNotificaciones
                                        }
                                    }
                                },
                                onEliminar = {
                                    scope.launch {
                                        eliminarNotificacion(notif.id)
                                        cargarNotificaciones { listaNotificaciones ->
                                            notificaciones = listaNotificaciones
                                        }
                                    }
                                }
                            )
                        }
                    }

                    // Anteriores
                    if (notificacionesAgrupadas["anteriores"]?.isNotEmpty() == true) {
                        item {
                            Text(
                                "Anteriores",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textOnSecondaryPlatform,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        items(notificacionesAgrupadas["anteriores"] ?: emptyList()) { notif ->
                            TarjetaNotificacion(
                                notif,
                                onMarcarLeida = {
                                    scope.launch {
                                        marcarComoLeida(notif.id)
                                        cargarNotificaciones { listaNotificaciones ->
                                            notificaciones = listaNotificaciones
                                        }
                                    }
                                },
                                onEliminar = {
                                    scope.launch {
                                        eliminarNotificacion(notif.id)
                                        cargarNotificaciones { listaNotificaciones ->
                                            notificaciones = listaNotificaciones
                                        }
                                    }
                                }
                            )
                        }
                    }

                    // Espacio al final
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaNotificacion(
    notificacion: Notificacion,
    onMarcarLeida: () -> Unit,
    onEliminar: () -> Unit
) {
    // Colores del tema
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val textColor = MaterialTheme.colorScheme.onSecondary
    val errorColor = MaterialTheme.colorScheme.error

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notificacion.leida) Color(0xfff5f5f5) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Contenido principal
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Icono
                Surface(
                    modifier = Modifier.size(48.dp),
                    color = secondaryColor,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // Información
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.Top)
                ) {
                    Text(
                        notificacion.titulo,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        notificacion.mensaje,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        formatearFecha(notificacion.fecha),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // Badge si no está leída
                if (!notificacion.leida) {
                    Surface(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xff2196F3),
                        shape = RoundedCornerShape(50)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                "1",
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Botones de acción
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón marcar como leída
                if (!notificacion.leida) {
                    Button(
                        onClick = onMarcarLeida,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            Icons.Filled.DoneAll,
                            contentDescription = "Marcar como leída",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Leída",
                            fontSize = 12.sp
                        )
                    }
                }

                // Botón eliminar
                Button(
                    onClick = onEliminar,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = errorColor,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Eliminar",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Eliminar",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

fun cargarNotificaciones(callback: (List<Notificacion>) -> Unit) {
    val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()

    db.collection("usuarios")
        .document(usuarioId)
        .collection("notificaciones")
        .orderBy("fecha", Query.Direction.DESCENDING)
        .get()
        .addOnSuccessListener { snapshot ->
            val lista = snapshot.documents.map { doc ->
                Notificacion(
                    id = doc.id,
                    titulo = doc.getString("titulo") ?: "",
                    mensaje = doc.getString("mensaje") ?: "",
                    tipo = doc.getString("tipo") ?: "",
                    fecha = doc.getTimestamp("fecha")?.seconds ?: 0,
                    leida = doc.getBoolean("leida") ?: false
                )
            }
            callback(lista)
        }
        .addOnFailureListener {
            callback(emptyList())
        }
}

fun marcarComoLeida(notificacionId: String) {
    val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()

    db.collection("usuarios")
        .document(usuarioId)
        .collection("notificaciones")
        .document(notificacionId)
        .update("leida", true)
}

fun eliminarNotificacion(notificacionId: String) {
    val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()

    db.collection("usuarios")
        .document(usuarioId)
        .collection("notificaciones")
        .document(notificacionId)
        .delete()
}

fun agruparPorSemana(notificaciones: List<Notificacion>): Map<String, List<Notificacion>> {
    val ahora = System.currentTimeMillis() / 1000
    val unaSemanaAtras = ahora - (7 * 24 * 60 * 60)

    val estaSemana = notificaciones.filter { it.fecha >= unaSemanaAtras }
    val anteriores = notificaciones.filter { it.fecha < unaSemanaAtras }

    return mapOf(
        "estaSemana" to estaSemana,
        "anteriores" to anteriores
    )
}

fun formatearFecha(timestamp: Long): String {
    if (timestamp == 0L) return ""
    val fecha = Date(timestamp * 1000)
    val formato = SimpleDateFormat("dd/MMM/yy - HH:mm", Locale("es", "ES"))
    return formato.format(fecha)
}

// Función para contar notificaciones no leídas con listener en tiempo real
fun contarNotificacionesNoLeidas(callback: (Int) -> Unit) {
    val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()

    db.collection("usuarios")
        .document(usuarioId)
        .collection("notificaciones")
        .whereEqualTo("leida", false)
        .addSnapshotListener { snapshot, error ->
            if (error != null) {
                callback(0)
                return@addSnapshotListener
            }
            val count = snapshot?.size() ?: 0
            callback(count)
        }
}

// Composable para el ícono de campanita con badge
@Composable
fun IconoCampanaConBadge(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    var notificacionesCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        contarNotificacionesNoLeidas { count ->
            notificacionesCount = count
        }
    }

    Box(modifier = modifier) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notificaciones",
                tint = tint,
                modifier = Modifier.size(28.dp)
            )
        }

        if (notificacionesCount > 0) {
            Badge(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-4).dp, y = 8.dp)
            ) {
                Text(
                    text = if (notificacionesCount > 99) "99+" else notificacionesCount.toString(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}