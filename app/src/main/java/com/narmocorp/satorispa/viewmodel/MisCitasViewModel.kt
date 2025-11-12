package com.narmocorp.satorispa.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.narmocorp.satorispa.model.Cita
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class MisCitasViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Guarda la lista completa y sin filtros de las citas.
    private val _allCitas = MutableStateFlow<List<Cita>>(emptyList())

    // Expone la lista que se mostrará en la UI (puede estar filtrada)
    private val _citas = MutableStateFlow<List<Cita>>(emptyList())
    val citas: StateFlow<List<Cita>> = _citas.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchMisCitas()
    }

    /**
     * Filtra la lista de citas por una fecha específica.
     * @param fecha La fecha en formato "yyyy-MM-dd". Si es nulo, se limpia el filtro.
     */
    fun filterCitasByDate(fecha: String?) {
        if (fecha == null) {
            _citas.value = _allCitas.value
        } else {
            _citas.value = _allCitas.value.filter { it.fecha == fecha }
        }
    }

    private fun fetchMisCitas() {
        val currentUserId = auth.currentUser?.uid
        Log.d("CitasDebug", "Buscando citas con UID: $currentUserId")

        if (currentUserId == null) {
            Log.e("CitasDebug", "Usuario no autenticado.")
            _isLoading.value = false
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val citasSnapshot = db.collection("citas")
                    .whereEqualTo("cliente_id", currentUserId)
                    .orderBy("fecha")
                    .get()
                    .await()

                Log.d("CitasDebug", "Documentos devueltos por Firestore: ${citasSnapshot.size()}")

                val citasList = citasSnapshot.documents.mapNotNull { document ->
                    val servicioId = document.getString("servicio") ?: ""
                    val servicioDoc = db.collection("servicios").document(servicioId).get().await()
                    val servicioImagenUrl = servicioDoc.getString("imagen") ?: ""
                    val servicioNombre = servicioDoc.getString("servicio") ?: "Servicio Desconocido"
                    val clienteId = document.getString("cliente_id")
                    val clienteNombre = document.getString("cliente")
                    val terapeutaNombre = document.getString("terapeuta")

                    Cita(
                        id = document.id,
                        cliente_id = clienteId,
                        servicio = servicioId,
                        servicioNombre = servicioNombre,
                        servicioImagen = servicioImagenUrl,
                        imagenServicio = servicioImagenUrl,
                        fecha = document.getString("fecha") ?: "",
                        hora = document.getString("hora") ?: "",
                        telefono = document.getString("telefono") ?: "",
                        estado = document.getString("estado") ?: "Pendiente",
                        cliente = clienteNombre ?: "",
                        terapeuta = terapeutaNombre
                    )
                }

                // Guarda la lista completa y establece la lista inicial a mostrar.
                _allCitas.value = citasList
                _citas.value = citasList

                Log.d("CitasDebug", "Citas mostradas en UI: ${_citas.value.size}")

            } catch (e: Exception) {
                Log.e("CitasDebug", "Error fatal al cargar citas: ${e.message}", e)
                _allCitas.value = emptyList()
                _citas.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}