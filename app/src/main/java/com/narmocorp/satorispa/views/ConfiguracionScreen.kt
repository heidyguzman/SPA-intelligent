package com.narmocorp.satorispa.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.narmocorp.satorispa.controller.AuthController
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout

// 🔑 IMPORTS NECESARIOS AÑADIDOS PARA BORRAR CUENTA
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Delete

// =========================================================================
// FUNCIONES DE DISEÑO ORIGINALES (PRESERVADAS)
// =========================================================================

@Composable
fun SeccionTitulo(texto: String, color: Color) {
    Text(
        text = texto,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = color,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
fun OpcionConfiguracion(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit,
    textOnBackground: Color,
    textOnSurface: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = textOnBackground
            )
            Text(
                text = subtitulo,
                fontSize = 13.sp,
                color = textOnSurface.copy(alpha = 0.6f)
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = textOnSurface.copy(alpha = 0.6f),
            modifier = Modifier.size(20.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}


// =========================================================================
// FUNCIÓN PRINCIPAL CONFIGURACION SCREEN
// =========================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(navController: NavController) {
    val context = LocalContext.current
    val colorEsquema = MaterialTheme.colorScheme

    // 🔑 ESTADOS AÑADIDOS PARA BORRAR CUENTA
    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }
    var mostrarDialogoBorrarCuenta by remember { mutableStateOf(false) } // NUEVO
    var contrasenaBorrarCuenta by remember { mutableStateOf("") } // NUEVO
    var isLoadingBorrarCuenta by remember { mutableStateOf(false) } // NUEVO

    // Definir colores del tema para facilitar la corrección
    val primaryBrandColor = MaterialTheme.colorScheme.primary
    val secondaryBrandColor = MaterialTheme.colorScheme.secondary
    val textOnSecondaryPlatform = MaterialTheme.colorScheme.onSecondary
    val textOnBackground = MaterialTheme.colorScheme.onBackground
    val textOnSurface = MaterialTheme.colorScheme.onSurface

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header con esquinas redondeadas
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
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = textOnSecondaryPlatform,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Configuración",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textOnSecondaryPlatform
                )
            }

            // Contenido scrolleable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
            ) {
                // Sección: Cuenta
                SeccionTitulo("Cuenta", color = textOnBackground)
                Spacer(modifier = Modifier.height(8.dp))

                OpcionConfiguracion(
                    icono = Icons.Default.Person,
                    titulo = "Perfil",
                    subtitulo = "Editar información personal",
                    onClick = { navController.navigate("editar_perfil") },
                    textOnBackground = textOnBackground,
                    textOnSurface = textOnSurface
                )

                OpcionConfiguracion(
                    icono = Icons.Default.Lock,
                    titulo = "Cambiar contraseña",
                    subtitulo = "Actualizar credenciales de acceso",
                    onClick = { navController.navigate("cambiar_contrasena") },
                    textOnBackground = textOnBackground,
                    textOnSurface = textOnSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Sección: Información
                SeccionTitulo("Información", color = textOnBackground)
                Spacer(modifier = Modifier.height(8.dp))

                OpcionConfiguracion(
                    icono = Icons.Default.Description,
                    titulo = "Términos y condiciones",
                    subtitulo = "Políticas de uso",
                    onClick = { navController.navigate("terminos_condiciones") },
                    textOnBackground = textOnBackground,
                    textOnSurface = textOnSurface
                )

                OpcionConfiguracion(
                    icono = Icons.Default.PrivacyTip,
                    titulo = "Política de privacidad",
                    subtitulo = "Cómo usamos tus datos",
                    onClick = { navController.navigate("politica_privacidad") },
                    textOnBackground = textOnBackground,
                    textOnSurface = textOnSurface
                )
                OpcionConfiguracion(
                    icono = Icons.Default.Info, // Usamos un ícono de información
                    titulo = "Acerca de Satori Spa",
                    subtitulo = "Versión y detalles de la aplicación",
                    onClick = { navController.navigate("acerca_de") }, // Nueva ruta de navegación
                    textOnBackground = textOnBackground,
                    textOnSurface = textOnSurface
                )


                OpcionConfiguracion(
                    icono = Icons.Default.HelpOutline,
                    titulo = "Ayuda y Soporte",
                    subtitulo = "Preguntas frecuentes y contacto",
                    onClick = { navController.navigate("ayuda") },
                    textOnBackground = textOnBackground,
                    textOnSurface = textOnSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // BOTÓN CERRAR SESIÓN
                    Button(
                        onClick = { mostrarDialogoCerrarSesion = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar Sesión", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(16.dp)) // Espacio entre botones

                    // 🔑 NUEVO: BOTÓN DE BORRAR CUENTA (Color de Advertencia)
                    Button(
                        onClick = { mostrarDialogoBorrarCuenta = true }, // Activa el diálogo de borrar
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935), // Rojo Fuerte de Advertencia
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Borrar cuenta", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // DIÁLOGO DE CERRAR SESIÓN (Tu código original)
        if (mostrarDialogoCerrarSesion) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoCerrarSesion = false },
                icon = {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = colorEsquema.error,
                        modifier = Modifier.size(48.dp)
                    )
                },
                title = { Text("¿Cerrar Sesión?") },
                text = {
                    Text(
                        "Tendrás que volver a iniciar sesión para acceder a tu cuenta.",
                        color = colorEsquema.onSurface.copy(alpha = 0.8f)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            mostrarDialogoCerrarSesion = false
                            AuthController.cerrarSesion()
                            navController.navigate("start") {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorEsquema.error
                        )
                    ) {
                        Text("Sí, Cerrar Sesión", color = colorEsquema.onError)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { mostrarDialogoCerrarSesion = false }
                    ) {
                        Text("Cancelar", color = colorEsquema.primary)
                    }
                },
                containerColor = colorEsquema.surface
            )
        }

        // 🔑 NUEVO: Llama al Diálogo de Borrar Cuenta
        if (mostrarDialogoBorrarCuenta) {
            BorrarCuentaDialog(
                navController = navController,
                onDismiss = {
                    mostrarDialogoBorrarCuenta = false
                    contrasenaBorrarCuenta = "" // Limpiar contraseña al cerrar
                    isLoadingBorrarCuenta = false // Restablecer estado de carga
                },
                contrasena = contrasenaBorrarCuenta,
                onContrasenaChange = { contrasenaBorrarCuenta = it },
                isLoading = isLoadingBorrarCuenta,
                onLoadingChange = { isLoadingBorrarCuenta = it }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorrarCuentaDialog(
    navController: NavController,
    onDismiss: () -> Unit,
    contrasena: String,
    onContrasenaChange: (String) -> Unit,
    isLoading: Boolean,
    onLoadingChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val colorEsquema = MaterialTheme.colorScheme
    var contrasenaVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                tint = colorEsquema.error, // Icono de advertencia en rojo
                modifier = Modifier.size(48.dp)
            )
        },
        title = { Text("Confirmar Eliminación de Cuenta") },
        text = {
            Column {
                Text(
                    "Esta acción es **permanente** y eliminará todos tus datos. " +
                            "Para continuar, ingresa tu contraseña actual:",
                    color = colorEsquema.onSurface.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Contraseña
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = onContrasenaChange,
                    label = { Text("Contraseña actual") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

                    visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = colorEsquema.primary) },
                    trailingIcon = {
                        val icon = if (contrasenaVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                            Icon(icon, contentDescription = "Toggle password visibility")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (contrasena.isBlank()) {
                        Toast.makeText(context, "Por favor, ingresa tu contraseña.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    onLoadingChange(true) // Iniciar carga

                    // Llama a la función de eliminación segura en AuthController
                    AuthController.deleteUserAndData(
                        contrasenaActual = contrasena,
                        onSuccess = {
                            onDismiss() // Cerrar el diálogo
                            Toast.makeText(context, "Cuenta eliminada exitosamente.", Toast.LENGTH_LONG).show()
                            // Navegar al inicio (Login/Start) y limpiar el BackStack
                            navController.navigate("start") {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        },
                        onError = { mensaje ->
                            onLoadingChange(false) // Detener carga en caso de error
                            Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
                        }
                    )
                },
                enabled = contrasena.isNotBlank() && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorEsquema.error
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Borrar permanentemente", color = colorEsquema.onError)
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Cancelar", color = colorEsquema.primary)
            }
        },
        containerColor = colorEsquema.surface
    )
}
