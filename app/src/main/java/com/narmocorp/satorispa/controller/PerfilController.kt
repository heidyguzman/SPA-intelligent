package com.narmocorp.satorispa.controller

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.narmocorp.satorispa.model.User
import java.util.UUID

private const val TAG = "PerfilController"

object PerfilController {

    /**
     * Obtiene los datos del usuario actual desde Firestore
     */
    fun obtenerDatosUsuario(
        onSuccess: (User) -> Unit,
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

        db.collection("usuarios").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    try {
                        val user = User(
                            nombre = document.getString("nombre") ?: "",
                            apellido = document.getString("apellido") ?: "",
                            correo = document.getString("correo") ?: "",
                            rol = document.getString("rol") ?: "",
                            imagenUrl = document.getString("imagenUrl") ?: "",
                            createdAt = document.getTimestamp("createdAt")
                        )
                        onSuccess(user)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error al parsear datos del usuario", e)
                        onError("Error al cargar datos: ${e.message}")
                    }
                } else {
                    onError("No se encontraron datos del usuario")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al obtener datos del usuario", e)
                onError("Error al conectar con la base de datos: ${e.message}")
            }
    }

    /**
     * Actualiza el perfil del usuario incluyendo la imagen si se proporciona
     */
    fun actualizarPerfil(
        nombre: String,
        apellido: String,
        correo: String,
        imagenUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onError("No hay usuario autenticado")
            return
        }

        val uid = currentUser.uid

        // Si hay una imagen, primero subirla a Storage
        if (imagenUri != null) {
            subirImagenStorage(uid, imagenUri,
                onSuccess = { imageUrl ->
                    // Una vez subida la imagen, actualizar Firestore
                    actualizarDatosFirestore(uid, nombre, apellido, correo, imageUrl, onSuccess, onError)
                },
                onError = onError
            )
        } else {
            // Si no hay imagen, solo actualizar datos en Firestore
            actualizarDatosFirestore(uid, nombre, apellido, correo, null, onSuccess, onError)
        }
    }

    /**
     * Sube la imagen a Firebase Storage en la carpeta usuarios7
     */
    private fun subirImagenStorage(
        uid: String,
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        // Crear nombre único para la imagen
        val imageName = "perfil_${uid}_${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child("usuarios7/$imageName")

        Log.d(TAG, "Subiendo imagen a: usuarios7/$imageName")

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                Log.d(TAG, "Imagen subida exitosamente")
                // Obtener la URL de descarga
                imageRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        Log.d(TAG, "URL de descarga obtenida: $downloadUrl")
                        onSuccess(downloadUrl)
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error al obtener URL de descarga", e)
                        onError("Error al obtener URL de la imagen: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al subir imagen", e)
                onError("Error al subir la imagen: ${e.message}")
            }
    }

    /**
     * Actualiza los datos del usuario en Firestore y opcionalmente en Auth
     */
    private fun actualizarDatosFirestore(
        uid: String,
        nombre: String,
        apellido: String,
        correo: String,
        imagenUrl: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        // Preparar datos a actualizar
        val updates = mutableMapOf<String, Any>(
            "nombre" to nombre,
            "apellido" to apellido,
            "correo" to correo
        )

        // Si hay nueva URL de imagen, agregarla
        if (imagenUrl != null) {
            updates["imagenUrl"] = imagenUrl
        }

        Log.d(TAG, "Actualizando Firestore con datos: $updates")

        // Actualizar Firestore
        db.collection("usuarios").document(uid)
            .update(updates)
            .addOnSuccessListener {
                Log.d(TAG, "Firestore actualizado exitosamente")

                // Si el correo cambió, actualizar también en Firebase Auth
                if (currentUser != null && currentUser.email != correo) {
                    currentUser.updateEmail(correo)
                        .addOnSuccessListener {
                            Log.d(TAG, "Email actualizado en Authentication")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error al actualizar email en Auth", e)
                            onError("Datos guardados pero error al actualizar email: ${e.message}")
                        }
                } else {
                    onSuccess()
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al actualizar Firestore", e)
                onError("Error al guardar cambios: ${e.message}")
            }
    }

    /**
     * Elimina una imagen del Storage (útil si quieres eliminar la imagen anterior)
     */
    fun eliminarImagenStorage(
        imagenUrl: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val storage = FirebaseStorage.getInstance()
            val imageRef = storage.getReferenceFromUrl(imagenUrl)

            imageRef.delete()
                .addOnSuccessListener {
                    Log.d(TAG, "Imagen eliminada exitosamente")
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error al eliminar imagen", e)
                    onError("Error al eliminar imagen: ${e.message}")
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener referencia de imagen", e)
            onError("URL de imagen inválida")
        }
    }
}