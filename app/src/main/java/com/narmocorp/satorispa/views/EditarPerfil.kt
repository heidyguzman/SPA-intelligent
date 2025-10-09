package com.narmocorp.satorispa.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(
    modifier: Modifier = Modifier,
    usuario: com.narmocorp.satorispa.models.Usuario? = null,
    onNavigateBack: (() -> Unit)? = null,
    onSaveChanges: ((String, String, String, String) -> Unit)? = null
) {
    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var apellido by remember { mutableStateOf(usuario?.apellido ?: "") }
    var correo by remember { mutableStateOf(usuario?.correo ?: "") }
    var contrasena by remember { mutableStateOf("") }
    var verificacion by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }
    var mostrarVerificacion by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Colores personalizados para la selección de texto
    val customTextSelectionColors = TextSelectionColors(
        handleColor = Color(0xFF995D2D),
        backgroundColor = Color(0xFF995D2D).copy(alpha = 0.3f)
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Header con botón de retroceso
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFDBBBA6))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF995D2D))
                        .clickable { onNavigateBack?.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Configuración",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF1C1B1F)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Contenedor principal con bordes redondeados
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFDBBBA6))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Foto de perfil con icono de editar
                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            modifier = Modifier.size(80.dp),
                            tint = Color(0xFF666666)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { /* Cambiar foto */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar foto",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF1C1B1F)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Campo de Nombre
                CustomTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = "Nombre",
                    icon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Apellido
                CustomTextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = "Apellido",
                    icon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Correo
                CustomTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = "Correo",
                    icon = Icons.Default.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Contraseña
                CustomPasswordField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = "Contraseña",
                    mostrarPassword = mostrarContrasena,
                    onTogglePassword = { mostrarContrasena = !mostrarContrasena }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Verificación
                CustomPasswordField(
                    value = verificacion,
                    onValueChange = { verificacion = it },
                    label = "Verificación",
                    mostrarPassword = mostrarVerificacion,
                    onTogglePassword = { mostrarVerificacion = !mostrarVerificacion }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botón Guardar
                Button(
                    onClick = { /* Guardar cambios */ },
                    modifier = Modifier
                        .width(200.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF995D2D)
                    ),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "Guardar",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF666666)
            )
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color(0xFF995D2D),
            unfocusedBorderColor = Color(0xFFCCCCCC),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color.Black,
            // 💡 Agrega esta línea para cambiar el color de la etiqueta al enfocarse
            focusedLabelColor = Color(0xFF995D2D), // Color café de tu paleta
            unfocusedLabelColor = Color(0xFF666666) // Color gris para no enfocado
        )
    )
}
@Composable
fun CustomPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    mostrarPassword: Boolean,GIT
    onTogglePassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Contraseña",
                tint = Color(0xFF666666)
            )
        },
        trailingIcon = {
            IconButton(onClick = onTogglePassword) {
                Icon(
                    imageVector = if (mostrarPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (mostrarPassword) "Ocultar" else "Mostrar",
                    tint = Color(0xFF666666)
                )
            }
        },
        visualTransformation = if (mostrarPassword) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color(0xFF995D2D),
            unfocusedBorderColor = Color(0xFFCCCCCC),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color.Black,
            // 💡 Agrega esta línea para cambiar el color de la etiqueta al enfocarse
            focusedLabelColor = Color(0xFF995D2D), // Color café de tu paleta
            unfocusedLabelColor = Color(0xFF666666) // Color gris para no enfocado
        )
    )
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun EditarPerfilPreview() {
    EditarPerfilScreen()
}