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

    private val _allCitas = MutableStateFlow<List<Cita>>(emptyList())
    private val _citas = MutableStateFlow<List<Cita>>(emptyList())
    val citas: StateFlow<List<Cita>> = _citas.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchMisCitas()
    }

    fun filterCitasByDate(fecha: String?) {
        if (fecha == null) {
            _citas.value = _allCitas.value
        } else {
            _citas.value = _allCitas.value.filter { it.fecha == fecha }
        }
    }

    fun cancelCita(citaId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                db.collection("citas").document(citaId)
                    .update("estado", "Cancelada")
                    .addOnSuccessListener {
                        Log.d("CitasDebug", "Cita $citaId cancelada con éxito.")
                        fetchMisCitas() // Recarga las citas para reflejar el cambio
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        Log.e("CitasDebug", "Error al cancelar la cita $citaId", e)
                        onFailure("No se pudo cancelar la cita. Inténtalo de nuevo.")
                    }
            } catch (e: Exception) {
                Log.e("CitasDebug", "Excepción al cancelar la cita $citaId", e)
                onFailure("Error interno al cancelar la cita.")
            }
        }
    }

    private fun fetchMisCitas() {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId == null) {
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

                val citasList = citasSnapshot.documents.mapNotNull { document ->
                    val servicioId = document.getString("servicio") ?: ""
                    val servicioDoc = db.collection("servicios").document(servicioId).get().await()
                    val servicioImagenUrl = servicioDoc.getString("imagen") ?: ""
                    val servicioNombre = servicioDoc.getString("servicio") ?: "Servicio Desconocido"

                    Cita(
                        id = document.id,
                        cliente_id = document.getString("cliente_id"),
                        servicio = servicioId,
                        servicioNombre = servicioNombre,
                        servicioImagen = servicioImagenUrl,
                        imagenServicio = servicioImagenUrl,
                        fecha = document.getString("fecha") ?: "",
                        hora = document.getString("hora") ?: "",
                        telefono = document.getString("telefono") ?: "",
                        estado = document.getString("estado") ?: "Pendiente",
                        cliente = document.getString("cliente") ?: "",
                        terapeuta = document.getString("terapeuta"),
                        comentarios = document.getString("comentarios") // Leer el nuevo campo
                    )
                }

                _allCitas.value = citasList
                _citas.value = citasList

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