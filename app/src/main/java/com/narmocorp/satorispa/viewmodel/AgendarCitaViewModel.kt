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
 * isAvailable = true SOLO si no ha sido reservada por NADIE.
 */
data class HoraCitaDTO(
    val hora: String,
    // La hora está disponible si nadie (ni el cliente actual ni otros) la ha reservado.
    val isAvailable: Boolean,
    // La hora ha sido reservada por el cliente actual (para fines de distinción visual).
    val isBookedByMe: Boolean
) {
    // Es seleccionable si está disponible.
    val isSelectable: Boolean
        get() = isAvailable
}


class AgendarCitaViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _servicio = MutableStateFlow<Servicio?>(null)
    val servicio: StateFlow<Servicio?> = _servicio

    // Emite List<HoraCitaDTO>
    private val _horasDisponibles = MutableStateFlow<List<HoraCitaDTO>>(emptyList())
    val horasDisponibles: StateFlow<List<HoraCitaDTO>> = _horasDisponibles

    // Lista de todas las posibles franjas horarias (Maestro de Horarios)
    private val masterHorarios = listOf(
        "09:00",
        "10:00",
        "11:00",
        "12:00",
        "13:00",
        "15:00",
        "16:00",
        "17:00",
        "18:00"
    )

    /**
     * Carga los detalles del servicio usando su ID
     */
    fun fetchServicioDetails(servicioId: String) {
        viewModelScope.launch {
            db.collection("servicios").document(servicioId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        try {
                            val loadedService = Servicio(
                                id = document.id,
                                servicio = document.getString("servicio") ?: "",
                                categoria = document.getString("categoria") ?: "",
                                descripcion = document.getString("descripcion") ?: "",
                                duracion = document.getString("duracion") ?: "",
                                estado = document.getString("estado") ?: "",
                                imagen = document.getString("imagen") ?: "",
                                precio = document.getString("precio") ?: "0"
                            )
                            _servicio.value = loadedService
                        } catch (e: Exception) {
                            Log.e("AgendarCitaViewModel", "Error al convertir documento de servicio: ${document.id}", e)
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

    /**
     * Consulta las citas agendadas y calcula el estado de cada hora (isAvailable, isBookedByMe).
     */
    fun fetchHorasDisponibles(dateMillis: Long) {
        _horasDisponibles.value = emptyList()

        // 1. Obtener el ID del cliente logueado
        val clienteId = FirebaseAuth.getInstance().currentUser?.uid

        // 2. Si el clienteId es null, asumimos que no hay reservas por mí y mostramos todas como libres
        if (clienteId == null) {
            Log.w("AgendarCitaViewModel", "⚠️ ClienteId es NULL - Usuario no autenticado")
            _horasDisponibles.value = masterHorarios.map { hora ->
                HoraCitaDTO(hora = hora, isAvailable = true, isBookedByMe = false)
            }
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC") // 🔥 IMPORTANTE: Usar UTC
        val selectedDate = dateFormat.format(Date(dateMillis))

        Log.d("AgendarCitaViewModel", "🔍 Consultando horas para fecha: $selectedDate (millis: $dateMillis)")
        Log.d("AgendarCitaViewModel", "👤 Cliente ID: $clienteId")

        viewModelScope.launch {
            try {
                // 3. Consultar TODAS las citas ACTIVAS para esta fecha
                val bookedCitasResult = db.collection("citas")
                    .whereEqualTo("fecha", selectedDate)
                    .whereIn("estado", listOf("Confirmada", "Pendiente"))
                    .get()
                    .await()

                Log.d("AgendarCitaViewModel", "📋 Total de citas encontradas: ${bookedCitasResult.size()}")

                // 4. Clasificar las reservas:
                val bookedTimesByMe = mutableSetOf<String>()
                val bookedTimesByOthers = mutableSetOf<String>()

                bookedCitasResult.documents.forEach { document ->
                    val hora = document.getString("hora") ?: return@forEach
                    val idCitaCliente = document.getString("cliente_id")
                    val estado = document.getString("estado")

                    Log.d("AgendarCitaViewModel", "  ⏰ Cita: $hora | Cliente: $idCitaCliente | Estado: $estado")

                    if (idCitaCliente == clienteId) {
                        bookedTimesByMe.add(hora)
                        Log.d("AgendarCitaViewModel", "    ✅ Hora $hora reservada por MÍ")
                    } else {
                        bookedTimesByOthers.add(hora)
                        Log.d("AgendarCitaViewModel", "    ❌ Hora $hora reservada por OTRO cliente")
                    }
                }

                Log.d("AgendarCitaViewModel", "📊 Resumen:")
                Log.d("AgendarCitaViewModel", "  - Horas reservadas por mí: $bookedTimesByMe")
                Log.d("AgendarCitaViewModel", "  - Horas reservadas por otros: $bookedTimesByOthers")

                // 5. Mapear el maestro de horarios a los DTOs
                val horasResult = masterHorarios.map { hora ->
                    val isBookedByMe = hora in bookedTimesByMe
                    val isBookedByOther = hora in bookedTimesByOthers

                    // Una hora está disponible SÓLO si no ha sido reservada por NADIE
                    val isAvailable = !isBookedByMe && !isBookedByOther

                    Log.d("AgendarCitaViewModel", "  🕐 $hora -> Disponible: $isAvailable | Por mí: $isBookedByMe | Por otro: $isBookedByOther")

                    HoraCitaDTO(
                        hora = hora,
                        isAvailable = isAvailable,
                        isBookedByMe = isBookedByMe
                    )
                }

                _horasDisponibles.value = horasResult
                Log.d("AgendarCitaViewModel", "✅ Horas disponibles actualizadas: ${horasResult.size} horas")

            } catch (e: Exception) {
                Log.e("AgendarCitaViewModel", "❌ Error al consultar horas disponibles para $selectedDate", e)
                _horasDisponibles.value = emptyList()
            }
        }
    }

    /**
     * Función final para registrar la cita en Firebase.
     */
    fun registrarCita(
        servicioId: String,
        fecha: String,
        hora: String,
        telefono: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val clienteId = FirebaseAuth.getInstance().currentUser?.uid

        if (clienteId == null) {
            onFailure("Error: El usuario no está autenticado. Por favor, reinicia la sesión.")
            return
        }

        viewModelScope.launch {
            try {
                // --- VERIFICACIÓN DE DUPLICADOS PARA ESTE CLIENTE (Capa de seguridad) ---
                val existingCitas = db.collection("citas")
                    .whereEqualTo("cliente_id", clienteId)
                    .whereEqualTo("fecha", fecha)
                    .whereEqualTo("hora", hora)
                    .whereIn("estado", listOf("Confirmada", "Pendiente"))
                    .get()
                    .await()

                if (!existingCitas.isEmpty) {
                    onFailure("Error: Ya tienes una cita agendada para el ${fecha} a las ${hora}. Por favor, selecciona otro horario.")
                    return@launch
                }

                // --- REGISTRO DE CITA (Si no hay duplicados) ---

                val timestampFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                timestampFormatter.timeZone = TimeZone.getTimeZone("UTC")
                val currentTimestamp = timestampFormatter.format(Date())

                val citaData = hashMapOf(
                    "cliente_id" to clienteId,
                    "servicio" to servicioId,
                    "fecha" to fecha,
                    "hora" to hora,
                    "telefono" to telefono,
                    "createdAt" to currentTimestamp,
                    "updatedAt" to currentTimestamp,
                    "cliente" to null,
                    "estado" to "Pendiente"
                )

                db.collection("citas")
                    .add(citaData)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        Log.e("AgendarCitaViewModel", "Error al registrar la cita", e)
                        onFailure("Error al registrar la cita. Inténtalo de nuevo.")
                    }

            } catch (e: Exception) {
                Log.e("AgendarCitaViewModel", "Error en la verificación o registro de cita", e)
                onFailure("Error interno al procesar la cita.")
            }
        }
    }
}