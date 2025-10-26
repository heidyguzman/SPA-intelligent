package com.narmocorp.satorispa.views

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.narmocorp.satorispa.controller.AuthController

// Importación corregida o asumida para la funcionalidad
// Si usas AuthController, cámbiala. Si es tu archivo, descomenta:
// import com.narmocorp.satorispa.controller.cambiarContrasena
//import com.narmocorp.satorispa.controller.AuthController // << Asumo este controlador para la lógica

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CambiarContrasenaScreen(navController: NavController) {
    val context = LocalContext.current

    var contrasenaActual by remember { mutableStateOf("") }
    var contrasenaNueva by remember { mutableStateOf("") }
    var contrasenaConfirmacion by remember { mutableStateOf("") }

    var cargando by remember { mutableStateOf(false) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }

    // Validación de requisitos de contraseña
    val tieneMinimo8Caracteres = contrasenaNueva.length >= 8
    val tieneMayuscula = contrasenaNueva.any { it.isUpperCase() }
    val tieneNumero = contrasenaNueva.any { it.isDigit() }
    val contrasenaCoincide = contrasenaNueva == contrasenaConfirmacion && contrasenaConfirmacion.isNotEmpty()
    val todosRequisitosCumplidos = tieneMinimo8Caracteres && tieneMayuscula && tieneNumero

    // Colores del tema
    val primaryBrandColor = MaterialTheme.colorScheme.primary
    val secondaryBrandColor = MaterialTheme.colorScheme.secondary
    val textOnSecondaryPlatform = MaterialTheme.colorScheme.onSecondary // Blanco en Header (Dark Mode)
    val textOnBackground = MaterialTheme.colorScheme.onBackground       // Blanco en fondo (Dark Mode)
    val textOnSurface = MaterialTheme.colorScheme.onSurface             // Blanco en campos de texto (Dark Mode)
    val successColor = Color(0xFF4CAF50) // Color verde para el mensaje de éxito

    val validarContrasenaActual = contrasenaActual.isNotEmpty()
    val validarContrasenaNueva = contrasenaNueva.isNotEmpty() && todosRequisitosCumplidos
    val validarContrasenaConfirmacion = contrasenaConfirmacion.isNotEmpty() && contrasenaCoincide
    // El estado final de validez
    val formIsValid = validarContrasenaActual && validarContrasenaNueva && validarContrasenaConfirmacion

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header (Restaurado el diseño de la imagen)
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ICONO GRANDE (Restaurado el diseño de la imagen)
                Box(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
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

                Text(
                    "Por tu seguridad",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textOnBackground,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    "Tu contraseña debe tener al menos 8 caracteres e incluir letras y números.",
                    fontSize = 13.sp,
                    color = textOnBackground.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Contraseña Actual
                Text(
                    "Contraseña Actual",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textOnBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                CampoTextoContrasena(
                    valor = contrasenaActual,
                    onValorCambiado = { contrasenaActual = it },
                    label = "Contraseña Actual",
                    textOnSurface = textOnSurface
                    // YA NO SE NECESITAN LOS PARÁMETROS isValid o successColor
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Contraseña Nueva
                Text(
                    "Nueva Contraseña",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textOnBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // *************************************************************
                // CAMBIOS CLAVE: Campo de contraseña nueva con supportingText
                // *************************************************************
                CampoTextoContrasena(
                    valor = contrasenaNueva,
                    onValorCambiado = { contrasenaNueva = it },
                    label = "Nueva Contraseña",
                    textOnSurface = textOnSurface,
                    supportingText = {
                        if (contrasenaNueva.isNotEmpty() && todosRequisitosCumplidos) {
                            Text("Contraseña segura", color = successColor)
                        } else if (contrasenaNueva.isNotEmpty() && !todosRequisitosCumplidos) {
                            // Opcional: Si quieres un mensaje de error si no cumple,
                            // pero el checklist ya hace esta función visualmente.
                            // Puedes dejar esto vacío si solo quieres el mensaje de éxito.
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Contraseña de Confirmación
                Text(
                    "Confirmar Nueva Contraseña",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textOnBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                CampoTextoContrasena(
                    valor = contrasenaConfirmacion,
                    onValorCambiado = { contrasenaConfirmacion = it },
                    label = "Confirmar Nueva Contraseña",
                    textOnSurface = textOnSurface
                    // Dejamos este sin supportingText para no duplicar el mensaje de éxito
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Requisitos de Contraseña (Título y Lista)
                Text(
                    "Requisitos de Contraseña",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textOnBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, bottom = 10.dp)
                )

                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    RequisitoContrasena("Mínimo 8 caracteres", tieneMinimo8Caracteres)
                    RequisitoContrasena("Incluir al menos una mayúscula", tieneMayuscula)
                    RequisitoContrasena("Incluir al menos un número", tieneNumero)
                    RequisitoContrasena("Las contraseñas deben coincidir", contrasenaCoincide)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {

                        // Verifica que el formulario sea válido antes de proceder.
                        if (formIsValid) {
                            cargando = true

                            // LÓGICA DE CAMBIO DE CONTRASEÑA IMPLEMENTADA
                            AuthController.cambiarContrasena(
                                contrasenaActual = contrasenaActual,
                                contrasenaNueva = contrasenaNueva,
                                onSuccess = {
                                    cargando = false
                                    mostrarDialogoExito = true
                                },
                                onError = { mensaje ->
                                    cargando = false
                                    Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
                                }
                            )
                        } else {
                            // Mensaje si el formulario no es válido y el botón fue presionado
                            // (esto sólo ocurriría si el botón no estuviera deshabilitado correctamente)
                            Toast.makeText(context, "Por favor, verifica la contraseña actual y que la nueva cumpla con todos los requisitos y coincida.", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBrandColor,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    // 🔑 PROPIEDAD CLAVE: EL BOTÓN SÓLO SE HABILITA SI EL FORMULARIO ES VÁLIDO Y NO ESTÁ CARGANDO
                    enabled = formIsValid && !cargando
                ) {
                    if (cargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cambiando...")
                    } else {
                        Icon(
                            Icons.Default.VpnKey,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Cambiar Contraseña",
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
                navController.navigate("login") {
                    popUpTo("start") { inclusive = true }
                }
            },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = successColor, // Usar la variable de color de éxito
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    "¡Contraseña Actualizada!",
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoTextoContrasena(
    valor: String,
    onValorCambiado: (String) -> Unit,
    label: String,
    textOnSurface: Color,
    // *************************************************************
    // CAMBIOS CLAVE: Revertir y agregar supportingText slot
    supportingText: @Composable (() -> Unit)? = null
    // *************************************************************
) {
    var visibilidad by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = valor,
        onValueChange = onValorCambiado,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                Icons.Default.VpnKey,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            IconButton(onClick = { visibilidad = !visibilidad }) {
                Icon(
                    if (visibilidad) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (visibilidad) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        },
        visualTransformation = if (visibilidad) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            // Revertido a tu color secundario por defecto
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = textOnSurface,
            unfocusedTextColor = textOnSurface
        ),
        singleLine = true,
        // *************************************************************
        // Slot para el texto condicional
        supportingText = supportingText
        // *************************************************************
    )
}

@Composable
fun RequisitoContrasena(texto: String, cumplido: Boolean) {
    // Usar colores del tema para asegurar visibilidad en Dark/Light mode
    val successColor = MaterialTheme.colorScheme.primary
    val defaultColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    val color = if (cumplido) successColor else defaultColor

    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            if (cumplido) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            texto,
            fontSize = 13.sp,
            color = color
        )
    }
}