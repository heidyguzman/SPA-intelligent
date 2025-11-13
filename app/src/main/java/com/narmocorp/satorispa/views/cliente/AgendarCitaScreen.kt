package com.narmocorp.satorispa.views.cliente

import android.app.Activity
import android.util.Log
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.narmocorp.satorispa.model.Servicio
import com.narmocorp.satorispa.ui.theme.Satori_Success
import com.narmocorp.satorispa.viewmodel.AgendarCitaViewModel
import com.narmocorp.satorispa.viewmodel.HoraCitaDTO
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.narmocorp.satorispa.utils.PhoneAuthManager
import com.narmocorp.satorispa.utils.PhoneAuthListener


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarCitaScreen(
    navController: NavController,
    servicioId: String,
    viewModel: AgendarCitaViewModel = viewModel()
) {
    // ESTADOS Y REFERENCIAS
    val servicio by viewModel.servicio.collectAsState()
    val horasDisponibles by viewModel.horasDisponibles.collectAsState()

    // --- ESTADOS DE LA PANTALLA ---
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var selectedDateDbFormat by remember { mutableStateOf<String?>(null) }
    var selectedDateUiFormat by remember { mutableStateOf("Seleccionar Fecha") }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var hasSelectedDate by remember { mutableStateOf(false) } 
    var comentarios by remember { mutableStateOf("") }
    var isBooking by remember { mutableStateOf(false) } // Estado de carga para el botón principal

    // --- ESTADOS DE VALIDACIÓN DE TELÉFONO ---
    var telefono by remember { mutableStateOf("") }
    var isPhoneValidated by remember { mutableStateOf(false) }
    var showOtpField by remember { mutableStateOf(false) }
    var isSendingCode by remember { mutableStateOf(false) }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var otpCode by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val phoneAuthManager = remember {
        PhoneAuthManager(context as Activity)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    // --- CALLBACK PARA PHONE AUTH (INTEGRA PERSISTENCIA) ---
    val phoneAuthListener = remember {
        object : PhoneAuthListener {
            override fun onCodeSent(id: String) {
                isSendingCode = false
                verificationId = id
                showOtpField = true
                phoneError = null
                scope.launch { snackbarHostState.showSnackbar("Código enviado correctamente.") }
            }

            override fun onVerificationFailed(error: String) {
                isSendingCode = false
                showOtpField = false
                phoneError = "Verificación fallida: $error"
                Log.e("PhoneAuth", "Error de verificación: $error")
            }

            override fun onVerificationSuccess() {
                viewModel.saveVerifiedTelefono(
                    telefono = telefono,
                    onSuccess = {
                        phoneError = null
                        showOtpField = false
                        isPhoneValidated = true
                        scope.launch { snackbarHostState.showSnackbar("Teléfono verificado y guardado en tu perfil.") }
                    },
                    onFailure = { errorMsg ->
                        isPhoneValidated = true
                        scope.launch { snackbarHostState.showSnackbar("Éxito en verificación, pero falló al guardar: $errorMsg") }
                    }
                )
            }
        }
    }

    // --- Cargar datos iniciales ---
    LaunchedEffect(servicioId) {
        viewModel.fetchServicioDetails(servicioId)
        viewModel.fetchProfileTelefono { loadedTelefono, isValidated ->
            if (isValidated && loadedTelefono != null) {
                telefono = loadedTelefono
                isPhoneValidated = true
                scope.launch { snackbarHostState.showSnackbar("Teléfono recuperado y validado.") }
            }
        }
    }

    // Limpiar hora seleccionada si ya no es válida
    LaunchedEffect(horasDisponibles) {
        if (selectedTime != null && horasDisponibles.none { it.hora == selectedTime && it.isAvailable }) {
            selectedTime = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agendar Cita", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ChevronLeft, contentDescription = "Atrás")
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
                item { ServiceHeader(servicio = loadedService) }
                item { HorizontalDivider(Modifier.padding(vertical = 4.dp)) }
                item { DatePickerComponent(selectedDateText = selectedDateUiFormat, onOpenDialog = { showDatePickerDialog = true }) }
                item { TimeSelectionComponent(horas = horasDisponibles, selectedTime = selectedTime, onTimeSelected = { selectedTime = it }, hasSelectedDate = hasSelectedDate) }
                item {
                    TelefonoFieldWithValidation(
                        telefono = telefono,
                        onTelefonoChange = { telefono = it },
                        isPhoneValidated = isPhoneValidated,
                        showOtpField = showOtpField,
                        otpCode = otpCode,
                        onOtpChange = { otpCode = it },
                        phoneError = phoneError,
                        isSendingCode = isSendingCode,
                        onSendCodeClick = {
                            if (isPhoneValidated) {
                                scope.launch { snackbarHostState.showSnackbar("El teléfono ya está verificado.") }
                                return@TelefonoFieldWithValidation
                            }
                            if (telefono.length != 10) {
                                scope.launch { snackbarHostState.showSnackbar("Ingresa un número de 10 dígitos.") }
                                return@TelefonoFieldWithValidation
                            }
                            isSendingCode = true
                            phoneError = null
                            phoneAuthManager.verifyPhoneNumber(telefono, phoneAuthListener)
                        },
                        onVerifyOtpClick = {
                            phoneError = null
                            if (verificationId != null && otpCode.length == 6) {
                                phoneAuthManager.signInWithOtp(verificationId!!, otpCode, phoneAuthListener)
                            } else {
                                phoneError = "Código OTP inválido."
                            }
                        }
                    )
                }
                item {
                    OutlinedTextField(
                        value = comentarios,
                        onValueChange = { comentarios = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Comentarios (opcional)") },
                        placeholder = { Text("Ej: Enfocarse más en la espalda.") },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        maxLines = 3
                    )
                }
                item { HorizontalDivider(Modifier.padding(vertical = 4.dp)) }
                item {
                    AppointmentSummary(
                        servicio = loadedService,
                        isReadyToBook = selectedTime != null && selectedDateDbFormat != null && isPhoneValidated,
                        isPhoneValidated = isPhoneValidated,
                        isBooking = isBooking, // Pasar el estado de carga
                        onConfirmClick = {
                            if (selectedTime != null && selectedDateDbFormat != null && isPhoneValidated) {
                                showConfirmationDialog = true
                            } else {
                                val message = if (!isPhoneValidated) "Selecciona fecha, hora y verifica tu teléfono."
                                else "Selecciona fecha y hora para continuar."
                                scope.launch { snackbarHostState.showSnackbar(message) }
                            }
                        }
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
            title = { Text("Confirmar Agendamiento", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("¿Estás seguro de que deseas agendar el siguiente servicio?")
                    Spacer(modifier = Modifier.height(12.dp))
                    val priceText = if (loadedService.precio.startsWith("$")) loadedService.precio else "$${loadedService.precio}"
                    Text(
                        text = "${loadedService.servicio} $priceText",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text("Fecha: $selectedDateUiFormat")
                    Text("Hora: $selectedTime")
                    if (comentarios.isNotBlank()) {
                        Text("Comentarios: $comentarios")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    showConfirmationDialog = false
                    isBooking = true // <-- ACTIVAR EL ESTADO DE CARGA
                    viewModel.registrarCita(
                        servicioId = loadedService.id,
                        fecha = selectedDateDbFormat!!,
                        hora = selectedTime!!,
                        telefono = telefono,
                        comentarios = comentarios,
                        onSuccess = {
                            isBooking = false // <-- DESACTIVAR CARGA
                            showSuccessDialog = true
                        },
                        onFailure = { errorMsg ->
                            isBooking = false // <-- DESACTIVAR CARGA
                            scope.launch { snackbarHostState.showSnackbar(errorMsg) }
                        }
                    )
                }) { Text("Sí, Confirmar Cita") }
            },
            dismissButton = { TextButton(onClick = { showConfirmationDialog = false }) { Text("Cancelar") } }
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("¡Cita Agendada con Éxito!", fontWeight = FontWeight.Bold) },
            text = { Text("Tu cita ha sido registrada. Revisa la sección 'Mis Citas' para ver el estado.") },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    navController.popBackStack()
                }) { Text("Aceptar") }
            }
        )
    }

    if (showDatePickerDialog) {
        val startOfTodayUtcMillis = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = startOfTodayUtcMillis
        )

        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                Button(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        if (selectedMillis < startOfTodayUtcMillis) {
                            scope.launch { snackbarHostState.showSnackbar("No puedes seleccionar una fecha pasada.") }
                            return@Button
                        }

                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = selectedMillis }
                        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                            scope.launch { snackbarHostState.showSnackbar("No se permiten citas en fin de semana.") }
                            return@Button
                        }

                        val date = Date(selectedMillis)
                        val uiFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") }
                        val dbFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") }
                        selectedDateUiFormat = uiFormatter.format(date)
                        selectedDateDbFormat = dbFormatter.format(date)
                        hasSelectedDate = true
                        viewModel.fetchHorasDisponibles(selectedMillis)
                        selectedTime = null
                    }
                    showDatePickerDialog = false
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton(onClick = { showDatePickerDialog = false }) { Text("Cancelar") } }
        ) { DatePicker(state = datePickerState) }
    }
}


@Composable
fun AppointmentSummary(
    servicio: Servicio,
    isReadyToBook: Boolean,
    isPhoneValidated: Boolean,
    isBooking: Boolean, // Nuevo parámetro
    onConfirmClick: () -> Unit
) {
    val precioPorSesion = servicio.precio.toDoubleOrNull() ?: 0.0
    val total = precioPorSesion

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Costo Total:", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
            Text("$${String.format(Locale.getDefault(), "%.2f", total)}", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        }
        Button(
            onClick = onConfirmClick,
            enabled = isReadyToBook && !isBooking, // Se deshabilita si está cargando
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            if (isBooking) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 3.dp
                )
            } else {
                Text("Confirmar Cita", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        if (!isReadyToBook && !isBooking) {
            val errorText = if (isPhoneValidated) "Seleccione fecha y hora para confirmar."
            else "Selecciona fecha, hora y verifica tu teléfono para confirmar."
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

// El resto de los componentes no necesitan cambios

@Composable
fun ServiceHeader(servicio: Servicio) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = servicio.imagen,
                contentDescription = "Imagen de ${servicio.servicio}",
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(servicio.servicio, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                Text("Duración: ${servicio.duracion} min", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Card(
            modifier = Modifier.widthIn(min = 80.dp).height(40.dp).wrapContentWidth(Alignment.End),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        ) {
            Text(
                text = if (servicio.precio.startsWith("$")) servicio.precio else "$${servicio.precio}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
fun DatePickerComponent(selectedDateText: String, onOpenDialog: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Event, contentDescription = "Fecha", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp).padding(end = 4.dp))
            Text("Selecciona la Fecha", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
        }
        Card(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onOpenDialog),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(selectedDateText, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Icon(Icons.Outlined.Event, contentDescription = "Abrir Calendario", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun TimeSelectionComponent(
    horas: List<HoraCitaDTO>,
    selectedTime: String?,
    onTimeSelected: (String) -> Unit,
    hasSelectedDate: Boolean // Nuevo parámetro
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Schedule, contentDescription = "Hora", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp).padding(end = 4.dp))
            Text("Selecciona la Hora", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
        }
        if (!hasSelectedDate) {
            Text("Selecciona una fecha para ver los horarios.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium, fontSize = 15.sp)
        } else if (horas.isEmpty()) {
            Text("No hay horarios disponibles para esta fecha.", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Medium, fontSize = 15.sp)
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(horas) { horaDto ->
                    val isChipEnabled = horaDto.isAvailable
                    val isChipSelected = horaDto.hora == selectedTime
                    FilterChip(
                        selected = isChipSelected,
                        enabled = isChipEnabled,
                        onClick = { if (isChipEnabled) onTimeSelected(horaDto.hora) },
                        label = { Text(horaDto.hora, fontWeight = if (isChipEnabled) FontWeight.SemiBold else FontWeight.Normal) },
                        leadingIcon = if (!isChipEnabled) { { Icon(imageVector = Icons.Default.Lock, contentDescription = "Ocupado", modifier = Modifier.size(16.dp)) } } else null
                    )
                }
            }
            if (horas.any { !it.isAvailable }) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Info, contentDescription = "Información", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Text("Las horas en gris están ocupadas.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun TelefonoFieldWithValidation(
    telefono: String,
    onTelefonoChange: (String) -> Unit,
    isPhoneValidated: Boolean,
    showOtpField: Boolean,
    otpCode: String,
    onOtpChange: (String) -> Unit,
    phoneError: String?,
    isSendingCode: Boolean,
    onSendCodeClick: () -> Unit,
    onVerifyOtpClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Phone, contentDescription = "Teléfono", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp).padding(end = 4.dp))
            Text("Proporciona y Verifica tu Teléfono", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = telefono,
                onValueChange = { if (it.all(Char::isDigit) && it.length <= 10) onTelefonoChange(it) },
                label = { Text("Teléfono (10 dígitos)") },
                placeholder = { Text("1234567890") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium,
                singleLine = true,
                enabled = !isPhoneValidated
            )
            if (!isPhoneValidated) {
                Button(
                    onClick = onSendCodeClick,
                    enabled = telefono.length == 10 && !isSendingCode,
                    modifier = Modifier.height(56.dp).align(Alignment.CenterVertically)
                ) {
                    if (isSendingCode) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                    else Text(if (showOtpField) "Reenviar" else "Enviar")
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Teléfono Verificado",
                    tint = Satori_Success, // Usando el color de éxito desde el tema
                    modifier = Modifier.size(30.dp).align(Alignment.CenterVertically)
                )
            }
        }
        if (isPhoneValidated) {
            Text("Teléfono verificado. Puedes continuar.", color = Satori_Success, fontWeight = FontWeight.Medium)
        }
        if (phoneError != null) {
            Text(phoneError, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Medium)
        }
        if (showOtpField) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = otpCode,
                    onValueChange = { if (it.all(Char::isDigit) && it.length <= 6) onOtpChange(it) },
                    label = { Text("Código de 6 dígitos") },
                    placeholder = { Text("123456") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true
                )
                Button(
                    onClick = onVerifyOtpClick,
                    enabled = otpCode.length == 6,
                    modifier = Modifier.height(56.dp).align(Alignment.CenterVertically),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) { Text("Verificar") }
            }
        }
    }
}
