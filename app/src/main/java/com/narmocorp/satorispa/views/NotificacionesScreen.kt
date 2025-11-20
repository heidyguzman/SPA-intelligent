package com.narmocorp.satorispa.views

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.narmocorp.satorispa.R
import com.narmocorp.satorispa.ui.theme.Satori_Notification_Badge
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class Notificacion(
    val id: String = "",
    val titulo: String = "",
    val mensaje: String = "",
    val tipo: String = "",
    val fecha: Long = 0,
    val leida: Boolean = false
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificacionesScreen(navController: NavController) {
    var notificaciones by remember { mutableStateOf<List<Notificacion>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    // Estado para el Modo Selección
    var isSelectionMode by remember { mutableStateOf(false) }
    var seleccionados by remember { mutableStateOf(setOf<String>()) }

    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current // Para vibración al hacer long press

    // Colores del tema
    val primaryBrandColor = MaterialTheme.colorScheme.primary
    val secondaryBrandColor = MaterialTheme.colorScheme.secondary
    val textOnSecondaryPlatform = MaterialTheme.colorScheme.onSecondary
    val backgroundColor = MaterialTheme.colorScheme.background
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val errorColor = MaterialTheme.colorScheme.error

    // Manejar el botón "Atrás" físico del dispositivo
    BackHandler(enabled = isSelectionMode) {
        // Si está en modo selección, salir del modo
        isSelectionMode = false
        seleccionados = emptySet()
    }

    // Cargar notificaciones al iniciar
    LaunchedEffect(Unit) {
        scope.launch {
            cargarNotificaciones { listaNotificaciones ->
                notificaciones = listaNotificaciones
                cargando = false
            }
        }
    }

    fun recargarDatos() {
        scope.launch {
            cargarNotificaciones { lista -> notificaciones = lista }
            isSelectionMode = false
            seleccionados = emptySet()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // --- HEADER DINÁMICO ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(secondaryBrandColor)
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isSelectionMode) {
                    // MODO SELECCIÓN: Botón X y Contador
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                isSelectionMode = false
                                seleccionados = emptySet()
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Cancelar selección",
                                tint = textOnSecondaryPlatform,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "${seleccionados.size}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textOnSecondaryPlatform
                        )
                    }

                    // Botones de acción (Borrar)
                    Row {
                        // Opcional: Botón para seleccionar todo
                        IconButton(
                            onClick = {
                                if (seleccionados.size == notificaciones.size) {
                                    seleccionados = emptySet()
                                } else {
                                    seleccionados = notificaciones.map { it.id }.toSet()
                                }
                            }
                        ) {
                            Icon(
                                Icons.Filled.SelectAll,
                                contentDescription = "Seleccionar todo",
                                tint = textOnSecondaryPlatform
                            )
                        }

                        IconButton(
                            onClick = {
                                if (seleccionados.isNotEmpty()) {
                                    scope.launch {
                                        eliminarMultiplesNotificaciones(seleccionados.toList()) {
                                            recargarDatos()
                                        }
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Borrar seleccionados",
                                tint = errorColor
                            )
                        }
                    }
                } else {
                    // MODO NORMAL: Botón Atrás y Título
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                }
            }

            // --- LISTA ---
            if (cargando) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryBrandColor)
                }
            } else if (notificaciones.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = onBackgroundColor.copy(alpha = 0.2f)
                        )
                        Text(
                            "No hay notificaciones",
                            fontSize = 16.sp,
                            color = onBackgroundColor.copy(alpha = 0.5f)
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
                            Text(
                                "Esta semana",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textOnSecondaryPlatform,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        items(notificacionesAgrupadas["estaSemana"] ?: emptyList()) { notif ->
                            TarjetaNotificacionSelectable(
                                notificacion = notif,
                                isSelectionMode = isSelectionMode,
                                isSelected = seleccionados.contains(notif.id),
                                onLongClick = {
                                    if (!isSelectionMode) {
                                        isSelectionMode = true
                                        seleccionados = setOf(notif.id)
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                },
                                onClick = {
                                    if (isSelectionMode) {
                                        seleccionados = if (seleccionados.contains(notif.id)) {
                                            seleccionados - notif.id
                                        } else {
                                            seleccionados + notif.id
                                        }
                                        // Si deselecciona todo, podríamos salir del modo,
                                        // pero es mejor dejar que el usuario salga con la X o Atrás.
                                    }
                                },
                                onMarcarLeida = {
                                    scope.launch {
                                        marcarComoLeida(notif.id)
                                        recargarDatos()
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
                            TarjetaNotificacionSelectable(
                                notificacion = notif,
                                isSelectionMode = isSelectionMode,
                                isSelected = seleccionados.contains(notif.id),
                                onLongClick = {
                                    if (!isSelectionMode) {
                                        isSelectionMode = true
                                        seleccionados = setOf(notif.id)
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                },
                                onClick = {
                                    if (isSelectionMode) {
                                        seleccionados = if (seleccionados.contains(notif.id)) {
                                            seleccionados - notif.id
                                        } else {
                                            seleccionados + notif.id
                                        }
                                    }
                                },
                                onMarcarLeida = {
                                    scope.launch {
                                        marcarComoLeida(notif.id)
                                        recargarDatos()
                                    }
                                }
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TarjetaNotificacionSelectable(
    notificacion: Notificacion,
    isSelectionMode: Boolean,
    isSelected: Boolean,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
    onMarcarLeida: () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onSecondary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val backgroundRead = MaterialTheme.colorScheme.background

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            // combinedClickable maneja tanto el click simple como el largo
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notificacion.leida) backgroundRead else surfaceColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        // Borde azul si está seleccionado, gris tenue si no
        border = if (isSelected) BorderStroke(2.dp, primaryColor) else BorderStroke(1.dp, onSurfaceColor.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Checkbox animado (solo aparece en modo selección)
            AnimatedVisibility(
                visible = isSelectionMode,
                enter = expandHorizontally(animationSpec = tween(200)) + fadeIn(),
                exit = shrinkHorizontally(animationSpec = tween(200)) + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(48.dp), // Para alinear visualmente con el icono
                    contentAlignment = Alignment.Center
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = { onClick() } // Delegamos al onClick de la tarjeta
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Icono con fondo gris claro
                    Surface(
                        modifier = Modifier.size(48.dp),
                        color = Color.LightGray,
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

                    Column(modifier = Modifier.weight(1f)) {
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
                            color = onSurfaceColor.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            formatearFecha(notificacion.fecha),
                            fontSize = 12.sp,
                            color = onSurfaceColor.copy(alpha = 0.5f)
                        )
                    }

                    if (!notificacion.leida) {
                        Surface(
                            modifier = Modifier.size(12.dp),
                            color = Satori_Notification_Badge,
                            shape = RoundedCornerShape(50)
                        ) {}
                    }
                }

                // Botón de acción (solo si no estamos en modo selección para no causar toques accidentales)
                // O si prefieres que siga estando, puedes dejarlo, pero usualmente en modo selección se deshabilitan acciones internas.
                if (!notificacion.leida && !isSelectionMode) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onMarcarLeida,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp),
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
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Marcar como leída", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// --- FUNCIONES FIREBASE (Igual que antes) ---

fun cargarNotificaciones(callback: (List<Notificacion>) -> Unit) {
    val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()
    db.collection("usuarios").document(usuarioId).collection("notificaciones")
        .orderBy("fecha", Query.Direction.DESCENDING).get()
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
        }.addOnFailureListener { callback(emptyList()) }
}

fun marcarComoLeida(notificacionId: String) {
    val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    FirebaseFirestore.getInstance().collection("usuarios").document(usuarioId)
        .collection("notificaciones").document(notificacionId).update("leida", true)
}

fun eliminarMultiplesNotificaciones(ids: List<String>, onSuccess: () -> Unit) {
    val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()
    val batch = db.batch()
    ids.forEach { id ->
        val ref = db.collection("usuarios").document(usuarioId)
            .collection("notificaciones").document(id)
        batch.delete(ref)
    }
    batch.commit().addOnSuccessListener { onSuccess() }
}

fun agruparPorSemana(notificaciones: List<Notificacion>): Map<String, List<Notificacion>> {
    val ahora = System.currentTimeMillis() / 1000
    val unaSemanaAtras = ahora - (7 * 24 * 60 * 60)
    return mapOf(
        "estaSemana" to notificaciones.filter { it.fecha >= unaSemanaAtras },
        "anteriores" to notificaciones.filter { it.fecha < unaSemanaAtras }
    )
}

fun formatearFecha(timestamp: Long): String {
    if (timestamp == 0L) return ""
    val formato = SimpleDateFormat("dd/MMM/yy - HH:mm", Locale("es", "ES"))
    return formato.format(Date(timestamp * 1000))
}

fun contarNotificacionesNoLeidas(callback: (Int) -> Unit) {
    val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    FirebaseFirestore.getInstance().collection("usuarios").document(usuarioId)
        .collection("notificaciones").whereEqualTo("leida", false)
        .addSnapshotListener { snapshot, _ -> callback(snapshot?.size() ?: 0) }
}

@Composable
fun IconoCampanaConBadge(onClick: () -> Unit, modifier: Modifier = Modifier, tint: Color = MaterialTheme.colorScheme.primary) {
    var notificacionesCount by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) { contarNotificacionesNoLeidas { notificacionesCount = it } }
    Box(modifier = modifier) {
        IconButton(onClick = onClick) {
            Icon(Icons.Default.Notifications, "Notificaciones", tint = tint, modifier = Modifier.size(28.dp))
        }
        if (notificacionesCount > 0) {
            Badge(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White,
                modifier = Modifier.align(Alignment.TopEnd).offset(x = (-4).dp, y = 8.dp)
            ) { Text(if (notificacionesCount > 99) "99+" else notificacionesCount.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold) }
        }
    }
}