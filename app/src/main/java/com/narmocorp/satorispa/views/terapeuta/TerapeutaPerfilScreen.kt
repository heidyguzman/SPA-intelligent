package com.narmocorp.satorispa.views.terapeuta

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.narmocorp.satorispa.controller.PerfilController
import com.narmocorp.satorispa.viewmodel.TerapeutaHomeViewModel
import com.narmocorp.satorispa.viewmodel.UserState
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerapeutaPerfilScreen(
    navController: NavController,
    viewModel: TerapeutaHomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val userState by viewModel.userState.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var nuevaImagenUri by remember { mutableStateOf<Uri?>(null) }

    var subiendoFoto by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = context.getExternalFilesDir("Pictures")
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    var fileUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            fileUri?.let {
                nuevaImagenUri = it
                subiendoFoto = true
                PerfilController.actualizarPerfil(
                    nombre = nombre, apellido = apellido, correo = email, rol = rol, imagenUri = it,
                    onSuccess = {
                        subiendoFoto = false
                        Toast.makeText(context, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show()
                        viewModel.refreshUserDataSilently()
                    },
                    onError = { mensaje ->
                        subiendoFoto = false
                        nuevaImagenUri = null
                        Toast.makeText(context, "Error: $mensaje", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            nuevaImagenUri = it
            subiendoFoto = true
            PerfilController.actualizarPerfil(
                nombre = nombre, apellido = apellido, correo = email, rol = rol, imagenUri = it,
                onSuccess = {
                    subiendoFoto = false
                    Toast.makeText(context, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show()
                    viewModel.refreshUserDataSilently()
                },
                onError = { mensaje ->
                    subiendoFoto = false
                    nuevaImagenUri = null
                    Toast.makeText(context, "Error: $mensaje", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val file = createImageFile()
            val newFileUri = FileProvider.getUriForFile(context, "com.narmocorp.satorispa.provider", file)
            fileUri = newFileUri
            cameraLauncher.launch(newFileUri)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Cambiar foto de perfil") },
            text = { Text("Elige una opción") },
            confirmButton = {
                TextButton(
                    onClick = { 
                        showDialog = false
                        when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
                            PackageManager.PERMISSION_GRANTED -> {
                                val file = createImageFile()
                                val newFileUri = FileProvider.getUriForFile(context, "com.narmocorp.satorispa.provider", file)
                                fileUri = newFileUri
                                cameraLauncher.launch(newFileUri)
                            }
                            else -> {
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    }
                ) {
                    Text("Tomar foto")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showDialog = false
                        galleryLauncher.launch("image/*")
                     }
                ) {
                    Text("Galería")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 12.dp).clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.secondary).padding(vertical = 12.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.onSecondary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Perfil", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondary)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (userState) {
                is UserState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is UserState.Success -> {
                    val user = (userState as UserState.Success).user
                    nombre = user.nombre
                    apellido = user.apellido
                    email = user.correo
                    rol = user.rol
                    imagenUrl = user.imagenUrl

                    Box(
                        modifier = Modifier.padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.size(120.dp)) {
                            Box(
                                modifier = Modifier.size(120.dp).clip(CircleShape).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)).border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                val painter = if (nuevaImagenUri != null) {
                                    rememberAsyncImagePainter(nuevaImagenUri)
                                } else if (imagenUrl.isNotEmpty()) {
                                    rememberAsyncImagePainter(imagenUrl)
                                } else {
                                    null
                                }

                                if (painter != null) {
                                    Image(painter = painter, contentDescription = "Foto de perfil", modifier = Modifier.fillMaxSize().clip(CircleShape), contentScale = ContentScale.Crop)
                                } else {
                                    Icon(Icons.Default.Person, contentDescription = "Sin foto", modifier = Modifier.size(60.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                                }
                                if (subiendoFoto) {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                            }

                            Box(
                                modifier = Modifier.size(36.dp).align(Alignment.BottomEnd).clip(CircleShape).background(MaterialTheme.colorScheme.primary).clickable { if (!subiendoFoto) showDialog = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Cambiar foto", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
                            }
                        }
                    }

                    Text("Cambiar foto de perfil", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSecondary, fontWeight = FontWeight.Medium, modifier = Modifier.clickable { if (!subiendoFoto) showDialog = true })

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Información Personal", fontWeight = FontWeight.Bold,color = MaterialTheme.colorScheme.onSecondary, fontSize = 18.sp, modifier = Modifier.align(Alignment.Start))
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(value = nombre, onValueChange = {}, label = { Text("Nombre") }, leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(disabledTextColor = MaterialTheme.colorScheme.onSurface, disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), disabledLeadingIconColor = MaterialTheme.colorScheme.primary), enabled = false)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = apellido, onValueChange = {}, label = { Text("Apellido") }, leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(disabledTextColor = MaterialTheme.colorScheme.onSurface, disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), disabledLeadingIconColor = MaterialTheme.colorScheme.primary), enabled = false)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = email, onValueChange = {}, label = { Text("Correo electrónico") }, leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(disabledTextColor = MaterialTheme.colorScheme.onSurface, disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), disabledLeadingIconColor = MaterialTheme.colorScheme.primary), enabled = false)
                }
                is UserState.Error -> {
                    val message = (userState as UserState.Error).message
                    Text(text = message)
                }
            }
        }
    }
}
