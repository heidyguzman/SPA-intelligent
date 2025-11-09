package com.narmocorp.satorispa.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.narmocorp.satorispa.model.Cita
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class MisCitasViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _citas = MutableStateFlow<List<Cita>>(emptyList())
    val citas: StateFlow<List<Cita>> = _citas

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchMisCitas()
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

                    // Sub-consulta: Obtener la imagen y el nombre del servicio
                    val servicioDoc = db.collection("servicios").document(servicioId).get().await()

                    val servicioImagenUrl = servicioDoc.getString("imagen") ?: ""
                    val servicioNombre = servicioDoc.getString("servicio") ?: "Servicio Desconocido"

                    // Extraemos los campos sensibles a nulos de la cita
                    val clienteId = document.getString("cliente_id")
                    val clienteNombre = document.getString("cliente")
                    val terapeutaNombre = document.getString("terapeuta")

                    Cita(
                        id = document.id,
                        // String? -> String?
                        cliente_id = clienteId,
                        // String -> String (usamos ?: "" en la DB y en el modelo es String)
                        servicio = servicioId,
                        servicioNombre = servicioNombre,
                        servicioImagen = servicioImagenUrl, // Asignamos la URL al campo del cliente
                        // Línea AGREGADA: Asignamos la misma URL al campo del terapeuta para evitar errores.
                        imagenServicio = servicioImagenUrl,
                        fecha = document.getString("fecha") ?: "",
                        hora = document.getString("hora") ?: "",
                        telefono = document.getString("telefono") ?: "",
                        estado = document.getString("estado") ?: "Pendiente",
                        // String? -> String?
                        cliente = clienteNombre ?: "",
                        terapeuta = terapeutaNombre
                    )
                }

                _citas.value = citasList.filter { it.cliente_id == currentUserId }
                Log.d("CitasDebug", "Citas mostradas en UI: ${_citas.value.size}")

            } catch (e: Exception) {
                Log.e("CitasDebug", "Error fatal al cargar citas: ${e.message}", e)
                _citas.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}