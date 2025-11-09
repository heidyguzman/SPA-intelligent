package com.narmocorp.satorispa.views.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // <-- ¡IMPORTADO!
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale // <-- ¡IMPORTADO!
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage // <-- ¡IMPORTADO para la imagen!
import com.narmocorp.satorispa.model.Servicio
import com.narmocorp.satorispa.viewmodel.AgendarCitaViewModel
import com.narmocorp.satorispa.viewmodel.HoraCitaDTO
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// 🎨 COLORES EXACTOS como pantalla "Cambiar Contraseña"
val Satori_Primary = Color(0xFF995D2D) // Marrón para bordes e iconos

// ✨ MODO CLARO: Fondo blanco con bordes café (como Cambiar Contraseña)
val InputCardBackgroundLight = Color.White // Fondo BLANCO
val InputCardBorderLight = Color(0xFFDBBBA6) // Borde beige/café claro
val ErrorTextLight = Color(0xFFD32F2F) // Rojo para errores
val OnSurfaceTextLight = Color(0xFF1C1C1C) // Texto negro
val SecondaryTextLight = Color(0xFF666666) // Gris para textos secundarios
val PlaceholderTextLight = Color(0xFF999999) // Gris para placeholders

// ✨ MODO OSCURO: Campos grises con buen contraste
val InputCardBackgroundDark = Color(0xFF353535) // Gris más claro y visible
val InputCardBorderDark = Color(0xFF505050) // Borde más claro y visible
val ErrorTextDark = Color(0xFFFF5252) // Rojo brillante tipo Material
val OnSurfaceTextDark = Color(0xFFFFFFFF) // BLANCO puro para textos
val SecondaryTextDark = Color(0xFFCCCCCC) // Gris MÁS claro para "Duración"
val PlaceholderTextDark = Color(0xFF999999) // Gris claro para placeholders


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarCitaScreen(
    navController: NavController,
    servicioId: String,
    viewModel: AgendarCitaViewModel = viewModel()
) {
    // ESTADOS Y REFERENCIAS
    val isDarkTheme = isSystemInDarkTheme()
    val servicio by viewModel.servicio.collectAsState()

    // RECIBE la lista de DTOs
    val horasDisponibles by viewModel.horasDisponibles.collectAsState()

    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    var selectedDateDbFormat by remember { mutableStateOf<String?>(null) }
    var selectedDateUiFormat by remember { mutableStateOf("Seleccionar Fecha") }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var telefono by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 🎨 Colores dinámicos
    val currentInputCardBackgroundColor = if (isDarkTheme) InputCardBackgroundDark else InputCardBackgroundLight
    val currentInputCardBorderColor = if (isDarkTheme) InputCardBorderDark else InputCardBorderLight
    val currentErrorTextColor = if (isDarkTheme) ErrorTextDark else ErrorTextLight
    val currentTextColor = if (isDarkTheme) OnSurfaceTextDark else OnSurfaceTextLight
    val currentSecondaryTextColor = if (isDarkTheme) SecondaryTextDark else SecondaryTextLight
    val currentPlaceholderColor = if (isDarkTheme) PlaceholderTextDark else PlaceholderTextLight

    // Cargar el servicio al inicio
    LaunchedEffect(servicioId) {
        viewModel.fetchServicioDetails(servicioId)
    }

    // Limpiar selectedTime si la hora ya no es agendable/válida.
    LaunchedEffect(horasDisponibles) {
        if (selectedTime != null) {
            val selectedDto = horasDisponibles.find { it.hora == selectedTime }

            // Si no se encuentra O no está disponible (isAvailable=false), la limpiamos.
            if (selectedDto == null || !selectedDto.isAvailable) {
                selectedTime = null
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Agendar Cita",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ChevronLeft,
                            contentDescription = "Atrás",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        servicio?.let { loadedService ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                // --- 1. Servicio Seleccionado (AQUÍ SE MUESTRA LA IMAGEN) ---
                item {
                    ServiceHeader(
                        servicio = loadedService,
                        cardBackgroundColor = currentInputCardBackgroundColor,
                        secondaryTextColor = currentSecondaryTextColor,
                        textColor = currentTextColor
                    )
                }

                item { HorizontalDivider(Modifier.padding(vertical = 4.dp)) }

                // --- 2. Selección de Fecha ---
                item {
                    DatePickerComponent(
                        selectedDateText = selectedDateUiFormat,
                        onOpenDialog = { showDatePickerDialog = true },
                        cardBackgroundColor = currentInputCardBackgroundColor,
                        borderColor = currentInputCardBorderColor,
                        textColor = currentTextColor,
                        secondaryTextColor = currentSecondaryTextColor

                    )
                }

                // --- 3. Selección de Hora ---
                item {
                    // PASAMOS la lista de DTOs
                    TimeSelectionComponent(
                        horas = horasDisponibles,
                        selectedTime = selectedTime,
                        onTimeSelected = { selectedTime = it },
                        errorTextColor = currentErrorTextColor
                    )
                }

                // --- 4. Teléfono ---
                item {
                    TelefonoField(
                        telefono = telefono,
                        onTelefonoChange = { telefono = it },
                        inputBackgroundColor = currentInputCardBackgroundColor,
                        borderColor = currentInputCardBorderColor,
                        textColor = currentTextColor,
                        secondaryTextColor = currentSecondaryTextColor,
                        placeholderColor = currentPlaceholderColor
                    )
                }

                item { HorizontalDivider(Modifier.padding(vertical = 4.dp)) }

                // --- 5. Resumen y Confirmación ---
                item {
                    AppointmentSummary(
                        servicio = loadedService,
                        quantity = 1,
                        isReadyToBook = selectedTime != null && selectedDateDbFormat != null && telefono.isNotBlank(),
                        onConfirmClick = {
                            if (selectedTime != null && selectedDateDbFormat != null && telefono.isNotBlank()) {
                                showConfirmationDialog = true
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Selecciona fecha, hora y proporciona tu teléfono.")
                                }
                            }
                        },
                        errorTextColor = currentErrorTextColor
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    // --- DIÁLOGOS ---
    if (showConfirmationDialog && servicio != null) {
        val loadedService = servicio!!
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = {
                Text("Confirmar Agendamiento", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text("¿Estás seguro de que deseas agendar el siguiente servicio?")
                    Spacer(modifier = Modifier.height(12.dp))

                    val priceText = if (loadedService.precio.startsWith("$")) loadedService.precio
                    else "\$${loadedService.precio}" 
                    Text(
                        text = "${loadedService.servicio} $priceText",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text("Fecha: $selectedDateUiFormat")
                    Text("Hora: $selectedTime")
                }
            },
            confirmButton = {
                Button(onClick = {
                    showConfirmationDialog = false

                    viewModel.registrarCita(
                        servicioId = loadedService.id,
                        fecha = selectedDateDbFormat!!,
                        hora = selectedTime!!,
                        telefono = telefono,
                        onSuccess = {
                            showSuccessDialog = true
                        },
                        onFailure = { errorMsg ->
                            scope.launch { snackbarHostState.showSnackbar(errorMsg) }
                        }
                    )
                }) {
                    Text("Sí, Confirmar Cita")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { /* No se puede cerrar hasta que presione Aceptar */ },
            title = { Text("¡Cita Agendada con Éxito!", fontWeight = FontWeight.Bold) },
            text = { Text("Tu cita ha sido registrada y está pendiente de confirmación. Revisa la sección 'Mis Citas' para ver el estado.") },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    navController.popBackStack()
                }) {
                    Text("Aceptar")
                }
            }
        )
    }

    // --- CÓDIGO CORREGIDO DEL DATE PICKER ---
    if (showDatePickerDialog) {
        val today = Calendar.getInstance().apply {
            time = Date()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val todayUtcMillis = today.timeInMillis - TimeZone.getDefault().rawOffset

        val dateValidator: (Long) -> Boolean = { dateMillis ->
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = dateMillis }
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val isFutureOrToday = dateMillis >= todayUtcMillis
            val isNotWeekend = dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
            isFutureOrToday && isNotWeekend
        }

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = todayUtcMillis,
        )

        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                Button(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis

                    if (selectedMillis != null) {
                        if (!dateValidator(selectedMillis)) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Fecha inválida: No se permiten días pasados ni fines de semana (Sáb/Dom)."
                                )
                            }
                            return@Button
                        }

                        val date = Date(selectedMillis)

                        val uiFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        val dbFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                        // SOLUCIÓN DE ZONA HORARIA: Forzar UTC en los formateadores
                        uiFormatter.timeZone = TimeZone.getTimeZone("UTC")
                        dbFormatter.timeZone = TimeZone.getTimeZone("UTC")

                        selectedDateUiFormat = uiFormatter.format(date)
                        selectedDateDbFormat = dbFormatter.format(date)

                        viewModel.fetchHorasDisponibles(selectedMillis)
                        selectedTime = null
                    }
                    showDatePickerDialog = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


// --- COMPONENTES ---

@Composable
fun ServiceHeader(
    servicio: Servicio,
    cardBackgroundColor: Color,
    secondaryTextColor: Color,
    textColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // IMAGEN ASÍNCRONA DEL SERVICIO
            AsyncImage(
                model = servicio.imagen, // <-- Usando la propiedad 'imagen'
                contentDescription = "Imagen de ${servicio.servicio}",
                modifier = Modifier
                    .size(64.dp) // Tamaño fijo
                    .clip(RoundedCornerShape(8.dp)), // Bordes redondeados
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp)) // Espacio entre imagen y texto

            Column(Modifier.weight(1f)) {
                Text(
                    servicio.servicio,
                    fontSize = 20.sp, // Ajuste para el espacio
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Duración: ${servicio.duracion} min",
                    fontSize = 14.sp,
                    color = secondaryTextColor,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        // CARD DE PRECIO
        Card(
            modifier = Modifier
                .widthIn(min = 80.dp)   // ancho mínimo; cambiar por .width(...) o .size(...) según necesites
                .height(40.dp)
                .wrapContentWidth(Alignment.End),
            colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = if (servicio.precio.startsWith("\$")) servicio.precio else "\$${servicio.precio}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
fun DatePickerComponent(
    selectedDateText: String,
    onOpenDialog: () -> Unit,
    cardBackgroundColor: Color,
    borderColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Outlined.Event,
                contentDescription = "Fecha",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp).padding(end = 4.dp)
            )
            Text(
                "Selecciona la Fecha",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onOpenDialog),
            colors = CardDefaults.cardColors(
                containerColor = cardBackgroundColor
            ),
            border = BorderStroke(
                width = 1.dp,
                color = borderColor
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedDateText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = secondaryTextColor
                )
                Icon(
                    Icons.Outlined.Event,
                    contentDescription = "Abrir Calendario",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun TimeSelectionComponent(
    horas: List<HoraCitaDTO>,
    selectedTime: String?,
    onTimeSelected: (String) -> Unit,
    errorTextColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.Schedule,
                contentDescription = "Hora",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp).padding(end = 4.dp)
            )
            Text(
                "Selecciona la Hora",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (horas.isEmpty()) {
            Text(
                text = "Selecciona una fecha para ver los horarios disponibles.",
                color = errorTextColor,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(horas) { horaDto ->

                    val isChipEnabled = horaDto.isAvailable
                    val isChipSelected = horaDto.hora == selectedTime

                    val disabledGrayColor = Color(0xFFE0E0E0)
                    val disabledTextColor = Color(0xFF9E9E9E)

                    val containerColor = when {
                        isChipEnabled && isChipSelected -> MaterialTheme.colorScheme.primary
                        !isChipEnabled -> disabledGrayColor
                        else -> MaterialTheme.colorScheme.surface
                    }

                    val labelColor = when {
                        isChipEnabled && isChipSelected -> MaterialTheme.colorScheme.onPrimary
                        !isChipEnabled -> disabledTextColor
                        else -> MaterialTheme.colorScheme.onSurface
                    }

                    val borderStroke = if (!isChipEnabled) {
                        BorderStroke(1.5.dp, Color(0xFFBDBDBD))
                    } else if (isChipSelected) {
                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    } else {
                        BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    }

                    FilterChip(
                        selected = isChipSelected,
                        enabled = isChipEnabled,
                        onClick = {
                            if (isChipEnabled) {
                                onTimeSelected(horaDto.hora)
                            }
                        },
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    horaDto.hora,
                                    fontWeight = if (isChipEnabled) FontWeight.SemiBold else FontWeight.Normal,
                                    color = labelColor
                                )

                                // ÍCONO VISUAL para horas ocupadas
                                if (!isChipEnabled) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Ocupado",
                                        modifier = Modifier.size(16.dp),
                                        tint = disabledTextColor
                                    )
                                }
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = containerColor,
                            labelColor = labelColor,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = disabledGrayColor,
                            disabledLabelColor = disabledTextColor
                        ),
                        border = borderStroke
                    )
                }
            }

            // LEYENDA mejorada y más visible
            if (horas.any { !it.isAvailable }) {
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Información",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Las horas en gris están ocupadas y no pueden seleccionarse",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TelefonoField(
    telefono: String,
    onTelefonoChange: (String) -> Unit,
    inputBackgroundColor: Color,
    borderColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    placeholderColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.Phone,
                contentDescription = "Teléfono",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp).padding(end = 4.dp)
            )
            Text(
                "Proporciona tu Teléfono",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        OutlinedTextField(
            value = telefono,
            onValueChange = {
                if (it.all { char -> char.isDigit() } && it.length <= 10) {
                    onTelefonoChange(it)
                }
            },
            label = {
                Text(
                    "Teléfono de Contacto (10 dígitos)",
                    color = secondaryTextColor
                )
            },
            placeholder = {
                Text(
                    "1234567890",
                    color = placeholderColor
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = inputBackgroundColor,
                focusedTextColor = textColor,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,

                unfocusedContainerColor = inputBackgroundColor,
                unfocusedTextColor = textColor,
                unfocusedBorderColor = borderColor,
                unfocusedLabelColor = secondaryTextColor,

                cursorColor = MaterialTheme.colorScheme.primary,

                // Color del placeholder
                unfocusedPlaceholderColor = placeholderColor,
                focusedPlaceholderColor = placeholderColor
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            singleLine = true
        )
    }
}

@Composable
fun AppointmentSummary(
    servicio: Servicio,
    quantity: Int,
    isReadyToBook: Boolean,
    onConfirmClick: () -> Unit,
    errorTextColor: Color
) {
    val precioPorSesion = servicio.precio.toDoubleOrNull() ?: 0.0
    val total = precioPorSesion * quantity

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Costo Total:",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "$${String.format(Locale.getDefault(), "%.2f", total)}",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            onClick = onConfirmClick,
            enabled = isReadyToBook,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            )
        ) {
            Text(
                "Confirmar Cita",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (!isReadyToBook) {
            Text(
                text = "Selecciona fecha, hora y proporciona tu teléfono para confirmar.",
                color = errorTextColor,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}