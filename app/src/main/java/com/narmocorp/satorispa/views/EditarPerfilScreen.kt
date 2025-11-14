package com.narmocorp.satorispa.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Patterns
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.narmocorp.satorispa.controller.AuthController
import com.narmocorp.satorispa.controller.PerfilController
import com.narmocorp.satorispa.viewmodel.ClientHomeViewModel
import java.io.File
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(navController: NavController, clienteViewModel: ClientHomeViewModel = viewModel()) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var nuevaImagenUri by remember { mutableStateOf<Uri?>(null) }

    var cargando by remember { mutableStateOf(true) }
    var guardando by remember { mutableStateOf(false) }

    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mostrarDialogoCambioEmail by remember { mutableStateOf(false) }
    var nuevoEmailInput by remember { mutableStateOf("") }
    var contrasenaActualInput by remember { mutableStateOf("") }

    // --- ESTADOS Y LAUNCHERS PARA SELECCIÓN DE IMAGEN ---
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        nuevaImagenUri = uri
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            nuevaImagenUri = tempImageUri
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val uri = createTempImageUri(context)
            tempImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        PerfilController.obtenerDatosUsuario(
            onSuccess = { user ->
                nombre = user.nombre
                apellido = user.apellido
                email = user.correo
                rol = user.rol
                imagenUrl = user.imagenUrl
                cargando = false
            },
            onError = { mensaje ->
                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                cargando = false
            }
        )
    }

    val primaryBrandColor = MaterialTheme.colorScheme.primary
    val secondaryBrandColor = MaterialTheme.colorScheme.secondary
    val textOnSecondaryPlatform = MaterialTheme.colorScheme.onSecondary
    val textOnSurface = MaterialTheme.colorScheme.onSurface
    val textOnBackground = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
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
                            Icons.Default.ArrowBack,
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier.size(120.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.secondary)
                                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                val imageModel: Any? = nuevaImagenUri ?: if (imagenUrl.isNotEmpty()) imagenUrl else null

                                if (imageModel != null) {
                                    AsyncImage(
                                        model = imageModel,
                                        contentDescription = "Foto de perfil",
                                        modifier = Modifier.fillMaxSize().clip(CircleShape),
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

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .align(Alignment.BottomEnd)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable { showImageSourceDialog = true },
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
                            .clickable { showImageSourceDialog = true }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

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
                        trailingIcon = {
                            IconButton(onClick = {
                                mostrarDialogoCambioEmail = true
                                nuevoEmailInput = email
                                contrasenaActualInput = ""
                            }) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Cambiar correo",
                                    tint = primaryBrandColor
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = MaterialTheme.colorScheme.secondary,
                            disabledLabelColor = Color.Gray,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            disabledTextColor = textOnSurface
                        ),
                        singleLine = true,
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
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
                                rol = rol,
                                imagenUri = nuevaImagenUri,
                                onSuccess = {
                                    guardando = false
                                    Toast.makeText(
                                        context,
                                        "Perfil actualizado correctamente.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    nuevaImagenUri = null
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

    if (mostrarDialogoExito) {
        CorreoCambiadoExitoDialog(
            navController = navController,
            onDismiss = {
                mostrarDialogoExito = false
            }
        )
    }

    if (mostrarDialogoCambioEmail) {
        CambioEmailDialog(
            onDismiss = {
                mostrarDialogoCambioEmail = false
                nuevoEmailInput = ""
                contrasenaActualInput = ""
            },
            onConfirm = { nuevoEmail, contrasena ->
                guardando = true
                AuthController.cambiarCorreo(
                    nuevoCorreo = nuevoEmail,
                    contrasenaActual = contrasena,
                    onSuccess = {
                        mostrarDialogoCambioEmail = false
                        email = nuevoEmail
                        guardando = false
                        mostrarDialogoExito = true
                    },
                    onError = { mensaje ->
                        guardando = false
                        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
                    }
                )
            },
            isLoading = guardando,
            nuevoEmail = nuevoEmailInput,
            onNuevoEmailChange = { nuevoEmailInput = it },
            contrasenaActual = contrasenaActualInput,
            onContrasenaActualChange = { contrasenaActualInput = it },
        )
    }

    if (showImageSourceDialog) {
        ImageSourceChooserDialog(
            onDismiss = { showImageSourceDialog = false },
            onTakePhotoClick = {
                showImageSourceDialog = false
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                        val uri = createTempImageUri(context)
                        tempImageUri = uri
                        cameraLauncher.launch(uri)
                    }
                    else -> {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            },
            onChooseFromGalleryClick = {
                showImageSourceDialog = false
                galleryLauncher.launch("image/*")
            }
        )
    }
}


@Composable
private fun ImageSourceChooserDialog(
    onDismiss: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onChooseFromGalleryClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cambiar foto de perfil") },
        text = {
            Column {
                ListItem(
                    headlineContent = { Text("Tomar foto") },
                    leadingContent = { Icon(Icons.Default.CameraAlt, contentDescription = null) },
                    modifier = Modifier.clickable { onTakePhotoClick() }
                )
                ListItem(
                    headlineContent = { Text("Elegir de la galería") },
                    leadingContent = { Icon(Icons.Default.PhotoLibrary, contentDescription = null) },
                    modifier = Modifier.clickable { onChooseFromGalleryClick() }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

private fun createTempImageUri(context: Context): Uri {
    val file = File.createTempFile("JPEG_${System.currentTimeMillis()}_", ".jpg", context.externalCacheDir)
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CambioEmailDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
    isLoading: Boolean,
    nuevoEmail: String,
    onNuevoEmailChange: (String) -> Unit,
    contrasenaActual: String,
    onContrasenaActualChange: (String) -> Unit,
) {
    val primaryBrandColor = MaterialTheme.colorScheme.primary
    val textOnSurface = MaterialTheme.colorScheme.onSurface
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val errorColor = MaterialTheme.colorScheme.error

    val unfocusedBorderColorVisible = Color.LightGray.copy(alpha = 0.5f)

    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(nuevoEmail).matches()
    val isFormValid = isEmailValid && contrasenaActual.isNotEmpty()

    var contrasenaVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Email, contentDescription = null, tint = primaryBrandColor) },
        title = { Text("Cambiar Correo Electrónico") },
        containerColor = MaterialTheme.colorScheme.surface,
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Por seguridad, ingresa tu nuevo correo y tu contraseña actual.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = nuevoEmail,
                    onValueChange = onNuevoEmailChange,
                    label = { Text("Nuevo Correo") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = primaryBrandColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    isError = nuevoEmail.isNotEmpty() && !isEmailValid,
                    supportingText = {
                        if (nuevoEmail.isNotEmpty() && !isEmailValid) {
                            Text("Formato de correo inválido", color = errorColor)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBrandColor,
                        unfocusedBorderColor = unfocusedBorderColorVisible,
                        focusedTextColor = textOnSurface,
                        unfocusedTextColor = textOnSurface,
                        errorBorderColor = errorColor
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = contrasenaActual,
                    onValueChange = onContrasenaActualChange,
                    label = { Text("Contraseña Actual") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = primaryBrandColor) },
                    trailingIcon = {
                        IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                            Icon(
                                imageVector = if (contrasenaVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (contrasenaVisible) "Ocultar" else "Mostrar",
                                tint = primaryBrandColor
                            )
                        }
                    },
                    visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBrandColor,
                        unfocusedBorderColor = unfocusedBorderColorVisible,
                        focusedTextColor = textOnSurface,
                        unfocusedTextColor = textOnSurface
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (contrasenaActual.isBlank()) {
                        Toast.makeText(context, "Debes ingresar tu contraseña actual para confirmar.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    onConfirm(nuevoEmail, contrasenaActual)
                },
                enabled = isFormValid && !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = primaryBrandColor)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Cambiar Correo")
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Cancelar", color = primaryBrandColor)
            }
        }
    )
}


@Composable
fun CorreoCambiadoExitoDialog(
    navController: NavController,
    onDismiss: () -> Unit
) {
    val primaryBrandColor = MaterialTheme.colorScheme.primary
    val successColor = Color(0xFF4CAF50) // Verde

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = successColor,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                "¡Correo Actualizado!",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text("Tu correo electrónico ha sido cambiado exitosamente. Por seguridad, debes iniciar sesión de nuevo con tu nueva dirección.")
        },
        confirmButton = {
            Button(
                onClick = {
                    AuthController.cerrarSesion()
                    onDismiss()
                    navController.navigate("login") {
                        popUpTo("start") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryBrandColor
                )
            ) {
                Text("Ir a iniciar sesión", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoTextoPersonalizado(
    valor: String,
    onValorCambiado: (String) -> Unit,
    label: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    textOnSurface: Color
) {
    OutlinedTextField(
        value = valor,
        onValueChange = { nuevoValor ->
            val filteredValue = nuevoValor.filter {
                it.isLetter() || it.isWhitespace() || it.toString()
                    .matches("[ñÑáéíóúÁÉÍÓÚüÜ]".toRegex())
            }
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
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = tipoTeclado
        )
    )
}