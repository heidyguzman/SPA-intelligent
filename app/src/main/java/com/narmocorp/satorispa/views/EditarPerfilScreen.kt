package com.narmocorp.satorispa.views

import android.net.Uri
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.narmocorp.satorispa.controller.PerfilController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(navController: NavController) {
    val primaryBrandColor = Color(0xff995d2d)
    val secondaryBrandColor = Color(0xffdbbba6)
    val textOnSecondaryPlatform = Color(0xff71390c)

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var imagenUriSeleccionada by remember { mutableStateOf<Uri?>(null) }

    var cargandoDatos by remember { mutableStateOf(true) }
    var guardando by remember { mutableStateOf(false) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    // Launcher para seleccionar imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imagenUriSeleccionada = uri
        }
    }

    // Cargar datos del usuario al iniciar
    LaunchedEffect(Unit) {
        PerfilController.obtenerDatosUsuario(
            onSuccess = { user ->
                nombre = user.nombre
                apellido = user.apellido
                email = user.correo
                imagenUrl = user.imagenUrl
                cargandoDatos = false
            },
            onError = { error ->
                mensajeError = error
                cargandoDatos = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (cargandoDatos) {
            // Indicador de carga
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = primaryBrandColor
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(secondaryBrandColor)
                        .padding(vertical = 12.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = textOnSecondaryPlatform,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Editar Perfil",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textOnSecondaryPlatform
                    )
                }

                // Contenido scrolleable
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Foto de perfil
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier.size(120.dp)
                        ) {
                            // Imagen de perfil
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(secondaryBrandColor)
                                    .border(3.dp, primaryBrandColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    // Mostrar imagen seleccionada
                                    imagenUriSeleccionada != null -> {
                                        Image(
                                            painter = rememberAsyncImagePainter(imagenUriSeleccionada),
                                            contentDescription = "Foto de perfil seleccionada",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    // Mostrar imagen existente
                                    imagenUrl.isNotEmpty() -> {
                                        Image(
                                            painter = rememberAsyncImagePainter(imagenUrl),
                                            contentDescription = "Foto de perfil",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    // Mostrar icono por defecto
                                    else -> {
                                        Icon(
                                            Icons.Default.Person,
                                            contentDescription = "Foto de perfil",
                                            modifier = Modifier.size(60.dp),
                                            tint = textOnSecondaryPlatform
                                        )
                                    }
                                }
                            }

                            // Botón de cámara
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .align(Alignment.BottomEnd)
                                    .clip(CircleShape)
                                    .background(primaryBrandColor)
                                    .clickable {
                                        imagePickerLauncher.launch("image/*")
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = "Cambiar foto",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = if (imagenUriSeleccionada != null) "Nueva foto seleccionada" else "Cambiar foto de perfil",
                        fontSize = 13.sp,
                        color = primaryBrandColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                imagePickerLauncher.launch("image/*")
                            }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Información personal
                    Text(
                        "Información Personal",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textOnSecondaryPlatform
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    CampoTextoPersonalizado(
                        valor = nombre,
                        onValorCambiado = { nombre = it },
                        label = "Nombre",
                        icono = Icons.Default.Person
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CampoTextoPersonalizado(
                        valor = apellido,
                        onValorCambiado = { apellido = it },
                        label = "Apellido",
                        icono = Icons.Default.Person
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CampoTextoPersonalizado(
                        valor = email,
                        onValorCambiado = { email = it },
                        label = "Correo electrónico",
                        icono = Icons.Default.Email,
                        tipoTeclado = KeyboardType.Email
                    )

                    // Mostrar error si existe
                    if (mensajeError != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFEBEE)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color(0xFFD32F2F),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    mensajeError ?: "",
                                    fontSize = 13.sp,
                                    color = Color(0xFFD32F2F)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón de guardar
                    Button(
                        onClick = {
                            if (nombre.isBlank() || apellido.isBlank() || email.isBlank()) {
                                mensajeError = "Todos los campos son obligatorios"
                                return@Button
                            }

                            guardando = true
                            mensajeError = null

                            PerfilController.actualizarPerfil(
                                nombre = nombre,
                                apellido = apellido,
                                correo = email,
                                imagenUri = imagenUriSeleccionada,
                                onSuccess = {
                                    guardando = false
                                    mostrarDialogoExito = true
                                    imagenUriSeleccionada = null // Resetear imagen seleccionada
                                },
                                onError = { error ->
                                    guardando = false
                                    mensajeError = error
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryBrandColor
                        ),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !guardando
                    ) {
                        if (guardando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                Icons.Default.Save,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Guardar cambios",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    // Diálogo de éxito
    if (mostrarDialogoExito) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoExito = false
                navController.popBackStack()
            },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xff4CAF50),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    "¡Perfil actualizado!",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Los cambios en tu perfil se han guardado correctamente.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogoExito = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBrandColor
                    )
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoTextoPersonalizado(
    valor: String,
    onValorCambiado: (String) -> Unit,
    label: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    tipoTeclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorCambiado,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                icono,
                contentDescription = null,
                tint = Color(0xff995d2d)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xff995d2d),
            unfocusedBorderColor = Color(0xffdbbba6),
            focusedLabelColor = Color(0xff995d2d),
            cursorColor = Color(0xff995d2d)
        ),
        singleLine = true
    )
}