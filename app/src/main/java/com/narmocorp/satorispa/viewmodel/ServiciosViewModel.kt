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
                            val nombre = document.get("servicio")?.toString()?.trim() ?: ""
                            val categoria = document.get("categoria")?.toString()?.trim() ?: ""
                            val descripcion = document.get("descripcion")?.toString()?.trim() ?: ""
                            val estado = document.get("estado")?.toString()?.trim() ?: ""
                            val imagen = document.get("imagen")?.toString()?.trim() ?: ""

                            val precio = when (val p = document.get("precio")) {
                                is Number -> p.toDouble()
                                is String -> p.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0
                                else -> 0.0
                            }

                            Servicio(
                                id = document.id,
                                nombre = nombre,
                                categoria = categoria,
                                descripcion = descripcion,
                                estado = estado,
                                imagen = imagen,
                                precio = precio
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
