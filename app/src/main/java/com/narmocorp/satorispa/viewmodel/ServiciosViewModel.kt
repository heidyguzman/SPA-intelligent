package com.narmocorp.satorispa.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.narmocorp.satorispa.model.Servicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ServiciosViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _servicios = MutableStateFlow<List<Servicio>>(emptyList())
    val servicios: StateFlow<List<Servicio>> = _servicios

    init {
        fetchServicios()
    }

    private fun fetchServicios() {
        viewModelScope.launch {
            db.collection("servicios")
                .get()
                .addOnSuccessListener { result ->
                    val serviceList = result.documents.mapNotNull { document ->
                        try {
                            Servicio(
                                id = document.id,
                                servicio = document.get("servicio")?.toString() ?: "",
                                categoria = document.get("categoria")?.toString() ?: "",
                                descripcion = document.get("descripcion")?.toString() ?: "",
                                duracion = document.get("duracion")?.toString() ?: "",
                                estado = document.get("estado")?.toString() ?: "",
                                imagen = document.get("imagen")?.toString() ?: "",
                                precio = document.get("precio")?.toString() ?: "0"
                            )
                        } catch (e: Exception) {
                            Log.e("ServiciosViewModel", "Error converting document ${document.id}", e)
                            null
                        }
                    }
                    _servicios.value = serviceList
                }
                .addOnFailureListener { exception ->
                    Log.e("ServiciosViewModel", "Error fetching services", exception)
                }
        }
    }
}
