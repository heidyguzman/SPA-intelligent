package com.narmocorp.satorispa.controller

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.narmocorp.satorispa.model.Cita
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val TAG = "CitasController"

object CitasController {

    // Helper privado reutilizable para parsear fechas ISO 8601 o Timestamp a com.google.firebase.Timestamp
    private fun parseIsoToTimestamp(value: Any?): Timestamp? {
        if (value == null) return null
        if (value is Timestamp) return value
        if (value is String) {
            val patterns = listOf(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss'Z'"
            )
            for (p in patterns) {
                try {
                    val sdf = SimpleDateFormat(p, Locale.US)
                    sdf.timeZone = TimeZone.getTimeZone("UTC")
                    val d: Date? = sdf.parse(value)
                    if (d == null) continue
                    val ms = d.time
                    val seconds = ms / 1000
                    val nanos = ((ms % 1000).toInt()) * 1_000_000
                    return Timestamp(seconds, nanos)
                } catch (_: ParseException) {
                    // intentar siguiente patrón
                } catch (e: Exception) {
                    Log.w(TAG, "Error parseando fecha: $value", e)
                }
            }
            Log.w(TAG, "No se pudo parsear fecha ISO: $value")
        }
        return null
    }

    private fun mapCitasConNombres(citasDocs: List<com.google.firebase.firestore.DocumentSnapshot>, clientNames: Map<String, String>): List<Cita> {
        return citasDocs.map { doc ->
            val clienteId = doc.getString("cliente_id")
            val nombreCliente = clientNames[clienteId] ?: doc.getString("cliente")

            Cita(
                id = doc.id,
                cliente = nombreCliente ?: "",
                cliente_id = clienteId,
                createdAt = parseIsoToTimestamp(doc.get("createdAt")),
                estado = doc.getString("estado") ?: "",
                fecha = doc.getString("fecha") ?: "",
                hora = doc.getString("hora") ?: "",
                servicio = doc.getString("servicio") ?: "",
                telefono = doc.getString("telefono") ?: "",
                terapeuta = doc.getString("terapeuta"),
                updatedAt = parseIsoToTimestamp(doc.get("updatedAt"))
            )
        }
    }

    /**
     * Obtiene las citas del terapeuta autenticado, filtrando por su servicio asignado.
     */
    fun obtenerCitasTerapeuta(
        onSuccess: (List<Cita>) -> Unit,
        onError: (String) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onError("No hay usuario autenticado")
            return
        }

        val uid = currentUser.uid
        val db = FirebaseFirestore.getInstance()

        // 1. Obtener el servicio del terapeuta desde su perfil
        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { userDoc ->
                if (!userDoc.exists()) {
                    onError("El perfil del terapeuta no fue encontrado.")
                    return@addOnSuccessListener
                }
                val servicioId = userDoc.getString("terapeuta_servicio")
                if (servicioId.isNullOrEmpty()) {
                    Log.w(TAG, "El terapeuta no tiene un servicio asignado.")
                    onSuccess(emptyList()) // No hay servicio, no hay citas
                    return@addOnSuccessListener
                }

                // 2. Obtener las citas que correspondan a ese servicio
                db.collection("citas")
                    .whereEqualTo("servicio", servicioId)
                    .get()
                    .addOnSuccessListener { result ->
                        if (result.isEmpty) {
                            onSuccess(emptyList())
                            return@addOnSuccessListener
                        }
                        val citasDocs = result.documents
                        val clientIds = citasDocs.mapNotNull { it.getString("cliente_id") }.filter { it.isNotBlank() }.distinct()

                        if (clientIds.isEmpty()) {
                            onSuccess(mapCitasConNombres(citasDocs, emptyMap()))
                            return@addOnSuccessListener
                        }

                        db.collection("usuarios").whereIn(FieldPath.documentId(), clientIds).get()
                            .addOnSuccessListener { usersSnapshot ->
                                val clientNames = usersSnapshot.associate { it.id to (it.getString("nombre") ?: "") }
                                onSuccess(mapCitasConNombres(citasDocs, clientNames))
                            }
                            .addOnFailureListener {
                                onSuccess(mapCitasConNombres(citasDocs, emptyMap()))
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error al leer citas por servicio", e)
                        onError("Error al conectar con la base de datos: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al leer perfil de terapeuta", e)
                onError("Error al obtener datos del terapeuta: ${e.message}")
            }
    }

    /**
     * Escucha en tiempo real las citas correspondientes al servicio del terapeuta.
     * Devuelve la ListenerRegistration para permitir remover el listener.
     */
    fun escucharCitasTerapeutaRealtime(
        onUpdate: (List<Cita>) -> Unit,
        onError: (String) -> Unit
    ): ListenerRegistration? {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onError("No hay usuario autenticado")
            return null
        }
        val uid = currentUser.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { userDoc ->
                if (!userDoc.exists()) {
                    onError("El perfil del terapeuta no fue encontrado.")
                    return@addOnSuccessListener
                }
                val servicioId = userDoc.getString("terapeuta_servicio")
                if (servicioId.isNullOrEmpty()) {
                    onUpdate(emptyList())
                    return@addOnSuccessListener
                }

                db.collection("citas").whereEqualTo("servicio", servicioId)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            onError("Error en tiempo real: ${error.message}")
                            return@addSnapshotListener
                        }
                        if (snapshot == null || snapshot.isEmpty) {
                            onUpdate(emptyList())
                            return@addSnapshotListener
                        }

                        val citasDocs = snapshot.documents
                        val clientIds = citasDocs.mapNotNull { it.getString("cliente_id") }.filter { it.isNotBlank() }.distinct()

                        if (clientIds.isEmpty()) {
                            onUpdate(mapCitasConNombres(citasDocs, emptyMap()))
                            return@addSnapshotListener
                        }

                        db.collection("usuarios").whereIn(FieldPath.documentId(), clientIds).get()
                            .addOnSuccessListener { usersSnapshot ->
                                val clientNames = usersSnapshot.associate { it.id to (it.getString("nombre") ?: "") }
                                onUpdate(mapCitasConNombres(citasDocs, clientNames))
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "No se pudieron obtener los nombres de los clientes", e)
                                onUpdate(mapCitasConNombres(citasDocs, emptyMap()))
                            }
                    }
            }
            .addOnFailureListener { e ->
                onError("Error al obtener datos del terapeuta: ${e.message}")
            }

        return null
    }
}
