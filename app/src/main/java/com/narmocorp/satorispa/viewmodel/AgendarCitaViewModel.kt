package com.narmocorp.satorispa.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.narmocorp.satorispa.model.Servicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

/**
 * Data Transfer Object (DTO) que indica el estado de cada hora.
 */
data class HoraCitaDTO(
    val hora: String,
    val isAvailable: Boolean,
    val isBookedByMe: Boolean
) {
    val isSelectable: Boolean
        get() = isAvailable
}

class AgendarCitaViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _servicio = MutableStateFlow<Servicio?>(null)
    val servicio: StateFlow<Servicio?> = _servicio

    private val _isPhoneValidatedOnProfile = MutableStateFlow(false)
    val isPhoneValidatedOnProfile: StateFlow<Boolean> = _isPhoneValidatedOnProfile

    private val _horasDisponibles = MutableStateFlow<List<HoraCitaDTO>>(emptyList())
    val horasDisponibles: StateFlow<List<HoraCitaDTO>> = _horasDisponibles

    private val masterHorarios = listOf(
        "09:00", "10:00", "11:00", "12:00", "13:00", "15:00", "16:00", "17:00", "18:00"
    )

    fun fetchServicioDetails(servicioId: String) {
        viewModelScope.launch {
            db.collection("servicios").document(servicioId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        try {
                            _servicio.value = Servicio(
                                id = document.id,
                                servicio = document.getString("servicio") ?: "",
                                categoriaId = document.getString("categoriaId") ?: "",
                                categoriaNombre = document.getString("categoriaNombre") ?: "",
                                descripcion = document.getString("descripcion") ?: "",
                                duracion = document.get("duracion")?.toString() ?: "",
                                estado = document.getString("estado") ?: "",
                                imagen = document.getString("imagen") ?: "",
                                precio = document.get("precio")?.toString() ?: "0"
                            )
                        } catch (e: Exception) {
                            Log.e("AgendarCitaViewModel", "Error al convertir documento: ${document.id}", e)
                            _servicio.value = null
                        }
                    } else {
                        _servicio.value = null
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("AgendarCitaViewModel", "Error al cargar servicio $servicioId", exception)
                    _servicio.value = null
                }
        }
    }

    // --- CORRECCIÓN CRÍTICA AQUÍ ---
    fun fetchHorasDisponibles(dateMillis: Long) {
        _horasDisponibles.value = emptyList()
        val clienteId = FirebaseAuth.getInstance().currentUser?.uid

        // 1. Formateador para la fecha seleccionada (viene en UTC desde el DatePicker)
        // Esto genera strings tipo "2025-11-28" basados en lo que el usuario vio en el calendario.
        val selectedDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val selectedDateString = selectedDateFormatter.format(Date(dateMillis))

        // 2. Formateador para "HOY" en la zona horaria del dispositivo (Local)
        val localDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault() // Hora local del celular
        }
        val todayDateString = localDateFormatter.format(Date())

        // 3. Comparación segura: ¿La fecha seleccionada (texto) es igual a hoy (texto)?
        val isToday = selectedDateString == todayDateString

        // 4. Filtrado de horas
        val relevantMasterHorarios = if (isToday) {
            // Si es HOY, obtenemos la hora actual del dispositivo y filtramos
            val currentHourInLocal = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            masterHorarios.filter { it.substringBefore(':').toInt() > currentHourInLocal }
        } else {
            // Si NO es hoy (es mañana o futuro), mostramos todos los horarios
            masterHorarios
        }

        if (relevantMasterHorarios.isEmpty()) {
            _horasDisponibles.value = emptyList()
            return
        }

        if (clienteId == null) {
            _horasDisponibles.value = relevantMasterHorarios.map { hora ->
                HoraCitaDTO(hora = hora, isAvailable = true, isBookedByMe = false)
            }
            return
        }

        Log.d("AgendarCitaViewModel", "Consultando horas para fecha: $selectedDateString (Es hoy: $isToday)")

        viewModelScope.launch {
            try {
                // Consultamos a Firebase usando el String correcto "yyyy-MM-dd"
                val bookedCitasResult = db.collection("citas")
                    .whereEqualTo("fecha", selectedDateString)
                    .whereIn("estado", listOf("Confirmada", "Pendiente"))
                    .get()
                    .await()

                val bookedTimes = bookedCitasResult.documents.mapNotNull { it.getString("hora") }.toSet()

                val horasResult = relevantMasterHorarios.map { hora ->
                    val isBooked = hora in bookedTimes
                    HoraCitaDTO(
                        hora = hora,
                        isAvailable = !isBooked,
                        isBookedByMe = isBooked && bookedCitasResult.documents.any { it.getString("hora") == hora && it.getString("cliente_id") == clienteId }
                    )
                }
                _horasDisponibles.value = horasResult

            } catch (e: Exception) {
                Log.e("AgendarCitaViewModel", "Error al consultar horas para $selectedDateString", e)
                _horasDisponibles.value = emptyList()
            }
        }
    }

    fun fetchProfileTelefono(onLoaded: (String?, Boolean) -> Unit) {
        val clienteId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val document = db.collection("usuarios").document(clienteId).get().await()
                if (document.exists()) {
                    val telefono = document.getString("telefono")
                    val isVerificado = document.getBoolean("telefonoVerificado") ?: false
                    if (isVerificado && !telefono.isNullOrBlank()) {
                        onLoaded(telefono, true)
                    } else {
                        onLoaded(telefono, false)
                    }
                } else {
                    onLoaded(null, false)
                }
            } catch (e: Exception) {
                Log.e("AgendarCitaViewModel", "Error al cargar teléfono del perfil", e)
                onLoaded(null, false)
            }
        }
    }

    fun saveVerifiedTelefono(telefono: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val clienteId = FirebaseAuth.getInstance().currentUser?.uid
        if (clienteId == null) {
            onFailure("Error de autenticación.")
            return
        }
        viewModelScope.launch {
            try {
                val userDataUpdate = mapOf("telefonoVerificado" to true, "telefono" to telefono)
                db.collection("usuarios").document(clienteId).update(userDataUpdate)
                    .addOnSuccessListener {
                        _isPhoneValidatedOnProfile.value = true
                        onSuccess()
                    }
                    .addOnFailureListener { e -> onFailure("Error al guardar el teléfono.") }
            } catch (e: Exception) {
                onFailure("Error interno al guardar teléfono.")
            }
        }
    }

    fun registrarCita(
        servicioId: String,
        fecha: String,
        hora: String,
        telefono: String,
        comentarios: String?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val clienteId = FirebaseAuth.getInstance().currentUser?.uid
        if (clienteId == null) {
            onFailure("Error: Usuario no autenticado.")
            return
        }
        viewModelScope.launch {
            try {
                val existingCitas = db.collection("citas")
                    .whereEqualTo("cliente_id", clienteId)
                    .whereEqualTo("fecha", fecha)
                    .whereEqualTo("hora", hora)
                    .whereIn("estado", listOf("Confirmada", "Pendiente"))
                    .get().await()

                if (!existingCitas.isEmpty) {
                    onFailure("Ya tienes una cita agendada para esta fecha y hora.")
                    return@launch
                }

                val terapeutasSnapshot = db.collection("usuarios")
                    .whereEqualTo("terapeuta_servicio", servicioId)
                    .limit(1)
                    .get()
                    .await()

                val terapeutaNombre = if (!terapeutasSnapshot.isEmpty) {
                    terapeutasSnapshot.documents[0].getString("nombre")
                } else {
                    null
                }

                val timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") }.format(Date())
                val citaData = hashMapOf<String, Any?>(
                    "cliente_id" to clienteId,
                    "servicio" to servicioId,
                    "fecha" to fecha,
                    "hora" to hora,
                    "telefono" to telefono,
                    "createdAt" to timestamp,
                    "updatedAt" to timestamp,
                    "cliente" to null,
                    "estado" to "Pendiente",
                    "terapeuta" to terapeutaNombre
                )

                if (!comentarios.isNullOrBlank()) {
                    citaData["comentarios"] = comentarios
                }

                db.collection("citas").add(citaData)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure("Error al registrar la cita.") }

            } catch (e: Exception) {
                onFailure("Error interno al procesar la cita.")
            }
        }
    }
}