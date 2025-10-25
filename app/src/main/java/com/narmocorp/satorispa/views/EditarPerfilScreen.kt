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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.widget.Toast
import coil.compose.rememberAsyncImagePainter
import com.narmocorp.satorispa.controller.PerfilController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(navController: NavController) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var nuevaImagenUri by remember { mutableStateOf<Uri?>(null) }

    var cargando by remember { mutableStateOf(true) }
    var guardando by remember { mutableStateOf(false) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }

    // Launcher para seleccionar imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        nuevaImagenUri = uri
    }

    // Cargar datos del perfil al iniciar
    LaunchedEffect(Unit) {
        PerfilController.obtenerDatosUsuario(
            onSuccess = { user ->
                nombre = user.nombre
                apellido = user.apellido
                email = user.correo
                imagenUrl = user.imagenUrl
                cargando = false
            },
            onError = { mensaje ->
                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                cargando = false
            }
        )
    }

    // Colores del tema (CORREGIDOS)
    val primaryBrandColor = MaterialTheme.colorScheme.primary
    val secondaryBrandColor = MaterialTheme.colorScheme.secondary
    val textOnSecondaryPlatform = MaterialTheme.colorScheme.onSecondary // Para el Header
    val textOnSurface = MaterialTheme.colorScheme.onSurface             // Para texto dentro de campos (blanco en Dark Mode)
    val textOnBackground = MaterialTheme.colorScheme.onBackground       // Para el texto principal (blanco en Dark Mode)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (cargando) {
            // Mostrar loading mientras carga los datos
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
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
                                    .background(MaterialTheme.colorScheme.secondary)
                                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                // Mostrar nueva imagen si se seleccionó, sino mostrar la actual
                                if (nuevaImagenUri != null) {
                                    Image(
                                        painter = rememberAsyncImagePainter(nuevaImagenUri),
                                        contentDescription = "Nueva foto de perfil",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else if (imagenUrl.isNotEmpty()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(imagenUrl),
                                        contentDescription = "Foto de perfil",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = "Sin foto",
                                        modifier = Modifier.size(60.dp),
                                        tint = textOnSecondaryPlatform
                                    )
                                }
                            }

                            // Botón de cámara
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .align(Alignment.BottomEnd)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable {
                                        imagePickerLauncher.launch("image/*")
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = "Cambiar foto",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Text(
                        "Cambiar foto de perfil",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
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
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textOnBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    CampoTextoPersonalizado(
                        valor = nombre,
                        onValorCambiado = { nombre = it },
                        label = "Nombre",
                        icono = Icons.Default.Person,
                        textOnSurface = textOnSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CampoTextoPersonalizado(
                        valor = apellido,
                        onValorCambiado = { apellido = it },
                        label = "Apellido",
                        icono = Icons.Default.Person,
                        textOnSurface = textOnSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email (solo lectura por ahora, ya que cambiar email requiere re-autenticación)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { },
                        label = { Text("Correo electrónico") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = MaterialTheme.colorScheme.secondary,
                            disabledLabelColor = Color.Gray,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            disabledTextColor = textOnSurface // Uso del color correcto
                        ),
                        singleLine = true,
                        enabled = false
                    )

                    Text(
                        "El correo no se puede modificar",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón de guardar
                    Button(
                        onClick = {
                            // Validaciones básicas
                            if (nombre.isEmpty() || apellido.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "El nombre y apellido son obligatorios",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            guardando = true

                            PerfilController.actualizarPerfil(
                                nombre = nombre,
                                apellido = apellido,
                                correo = email,
                                imagenUri = nuevaImagenUri,
                                onSuccess = {
                                    guardando = false
                                    mostrarDialogoExito = true
                                },
                                onError = { mensaje ->
                                    guardando = false
                                    Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !guardando
                    ) {
                        if (guardando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Guardando...")
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
                // Notificar al home que debe refrescar
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("profile_updated", true)
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
                        // Notificar al home que debe refrescar
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("profile_updated", true)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
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
    tipoTeclado: KeyboardType = KeyboardType.Text,
    textOnSurface: Color // Parámetro añadido para el color del texto
) {
    OutlinedTextField(
        value = valor,
        onValueChange = { nuevoValor ->
            // FILTRO: Permite solo letras, espacios y caracteres acentuados comunes
            val filteredValue = nuevoValor.filter { it.isLetter() || it.isWhitespace() || it.toString().matches("[ñÑáéíóúÁÉÍÓÚüÜ]".toRegex()) }
            onValorCambiado(filteredValue)
        },
        label = { Text(label) },
        leadingIcon = {
            Icon(
                icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = textOnSurface,
            unfocusedTextColor = textOnSurface
        ),
        singleLine = true
    )
}