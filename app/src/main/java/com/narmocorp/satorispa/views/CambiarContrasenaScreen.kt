package com.narmocorp.satorispa.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CambiarContrasenaScreen(navController: NavController) {
    val primaryBrandColor = Color(0xff995d2d)
    val secondaryBrandColor = Color(0xffdbbba6)
    val textOnSecondaryPlatform = Color(0xff71390c)

    var contrasenaActual by remember { mutableStateOf("") }
    var contrasenaNueva by remember { mutableStateOf("") }
    var contrasenaConfirmar by remember { mutableStateOf("") }

    var mostrarContrasenaActual by remember { mutableStateOf(false) }
    var mostrarContrasenaNueva by remember { mutableStateOf(false) }
    var mostrarContrasenaConfirmar by remember { mutableStateOf(false) }

    var errorContrasenaActual by remember { mutableStateOf<String?>(null) }
    var errorContrasenaNueva by remember { mutableStateOf<String?>(null) }
    var errorContrasenaConfirmar by remember { mutableStateOf<String?>(null) }

    var guardando by remember { mutableStateOf(false) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mostrarDialogoError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    // Validación en tiempo real
    val contrasenaValida = contrasenaNueva.length >= 8
    val contrasenasCoinciden = contrasenaNueva == contrasenaConfirmar && contrasenaConfirmar.isNotEmpty()
    val formularioValido = contrasenaActual.isNotEmpty() && contrasenaValida && contrasenasCoinciden

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
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
                    "Cambiar Contraseña",
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
                // Icono de seguridad
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(secondaryBrandColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Seguridad",
                            modifier = Modifier.size(50.dp),
                            tint = primaryBrandColor
                        )
                    }
                }

                Text(
                    "Por tu seguridad",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textOnSecondaryPlatform,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Tu contraseña debe tener al menos 8 caracteres e incluir letras y números.",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Contraseña actual
                SeccionTitulo("Contraseña Actual")
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = contrasenaActual,
                    onValueChange = {
                        contrasenaActual = it
                        errorContrasenaActual = null
                    },
                    label = { Text("Contraseña actual") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = primaryBrandColor
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { mostrarContrasenaActual = !mostrarContrasenaActual }) {
                            Icon(
                                if (mostrarContrasenaActual) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (mostrarContrasenaActual) "Ocultar" else "Mostrar",
                                tint = Color.Gray
                            )
                        }
                    },
                    visualTransformation = if (mostrarContrasenaActual)
                        VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBrandColor,
                        unfocusedBorderColor = secondaryBrandColor,
                        focusedLabelColor = primaryBrandColor,
                        cursorColor = primaryBrandColor,
                        errorBorderColor = Color.Red
                    ),
                    isError = errorContrasenaActual != null,
                    singleLine = true
                )

                if (errorContrasenaActual != null) {
                    Text(
                        errorContrasenaActual!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Nueva contraseña
                SeccionTitulo("Nueva Contraseña")
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = contrasenaNueva,
                    onValueChange = {
                        contrasenaNueva = it
                        errorContrasenaNueva = null
                    },
                    label = { Text("Nueva contraseña") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.VpnKey,
                            contentDescription = null,
                            tint = primaryBrandColor
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { mostrarContrasenaNueva = !mostrarContrasenaNueva }) {
                            Icon(
                                if (mostrarContrasenaNueva) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (mostrarContrasenaNueva) "Ocultar" else "Mostrar",
                                tint = Color.Gray
                            )
                        }
                    },
                    visualTransformation = if (mostrarContrasenaNueva)
                        VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBrandColor,
                        unfocusedBorderColor = secondaryBrandColor,
                        focusedLabelColor = primaryBrandColor,
                        cursorColor = primaryBrandColor,
                        errorBorderColor = Color.Red
                    ),
                    isError = errorContrasenaNueva != null || (contrasenaNueva.isNotEmpty() && !contrasenaValida),
                    singleLine = true
                )

                // Indicador de fortaleza
                if (contrasenaNueva.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (contrasenaValida) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (contrasenaValida) Color(0xff4CAF50) else Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            if (contrasenaValida) "Contraseña segura" else "Mínimo 8 caracteres",
                            fontSize = 12.sp,
                            color = if (contrasenaValida) Color(0xff4CAF50) else Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Confirmar contraseña
                OutlinedTextField(
                    value = contrasenaConfirmar,
                    onValueChange = {
                        contrasenaConfirmar = it
                        errorContrasenaConfirmar = null
                    },
                    label = { Text("Confirmar contraseña") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = primaryBrandColor
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { mostrarContrasenaConfirmar = !mostrarContrasenaConfirmar }) {
                            Icon(
                                if (mostrarContrasenaConfirmar) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (mostrarContrasenaConfirmar) "Ocultar" else "Mostrar",
                                tint = Color.Gray
                            )
                        }
                    },
                    visualTransformation = if (mostrarContrasenaConfirmar)
                        VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBrandColor,
                        unfocusedBorderColor = secondaryBrandColor,
                        focusedLabelColor = primaryBrandColor,
                        cursorColor = primaryBrandColor,
                        errorBorderColor = Color.Red
                    ),
                    isError = errorContrasenaConfirmar != null || (contrasenaConfirmar.isNotEmpty() && !contrasenasCoinciden),
                    singleLine = true
                )

                // Indicador de coincidencia
                if (contrasenaConfirmar.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (contrasenasCoinciden) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (contrasenasCoinciden) Color(0xff4CAF50) else Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            if (contrasenasCoinciden) "Las contraseñas coinciden" else "Las contraseñas no coinciden",
                            fontSize = 12.sp,
                            color = if (contrasenasCoinciden) Color(0xff4CAF50) else Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Requisitos de seguridad
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xfff5f5f5)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = primaryBrandColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Requisitos de seguridad",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textOnSecondaryPlatform
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        RequisitoContrasena("Mínimo 8 caracteres", contrasenaNueva.length >= 8)
                        RequisitoContrasena("Contiene letras", contrasenaNueva.any { it.isLetter() })
                        RequisitoContrasena("Contiene números", contrasenaNueva.any { it.isDigit() })
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón de guardar
                Button(
                    onClick = {
                        if (formularioValido) {
                            guardando = true
                            // Simular validación y guardado
                            kotlinx.coroutines.GlobalScope.launch {
                                kotlinx.coroutines.delay(1500)
                                guardando = false
                                // Simular éxito (en producción aquí iría la validación real)
                                mostrarDialogoExito = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBrandColor,
                        disabledContainerColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = formularioValido && !guardando
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
                            "Cambiar contraseña",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
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
                    "¡Contraseña actualizada!",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Tu contraseña ha sido cambiada exitosamente. Por favor, inicia sesión nuevamente con tu nueva contraseña.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogoExito = false
                        // Redirigir al login
                        navController.navigate("login") {
                            popUpTo("start") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBrandColor
                    )
                ) {
                    Text("Ir a iniciar sesión")
                }
            }
        )
    }
}

@Composable
fun RequisitoContrasena(texto: String, cumplido: Boolean) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            if (cumplido) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (cumplido) Color(0xff4CAF50) else Color.Gray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            texto,
            fontSize = 13.sp,
            color = if (cumplido) Color(0xff4CAF50) else Color.Gray
        )
    }
}