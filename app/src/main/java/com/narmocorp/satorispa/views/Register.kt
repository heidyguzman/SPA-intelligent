package com.narmocorp.satorispa.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.narmocorp.satorispa.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun Register(modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        // Fondo superior con imagen - REDUCIDO
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "fondo",
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.25f), // Reducido de 0.30f a 0.25f
            contentScale = ContentScale.Crop
        )

        // Logo - REDUCIDO
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.02f) // Reducido de 0.03f a 0.02f
                .size(screenWidth * 0.50f, screenHeight * 0.18f), // Reducido tamaño
            contentScale = ContentScale.Crop
        )

        // Card principal - AUMENTADO
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.98f)
                .fillMaxHeight(0.78f), // Aumentado de 0.72f a 0.78f
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {}

        // Tabs Inicio / Registro - AJUSTADO
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.27f) // Ajustado según nueva altura del fondo
                .fillMaxWidth(0.82f)
                .height(screenHeight * 0.09f) // Reducido de 0.11f a 0.09f
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(100.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xffdbbba6).copy(alpha = 0.86f))
            ) {}
            // Botón Inicio
            TextButton(
                onClick = { /* ir a Login */ },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = screenWidth * 0.08f, top = screenHeight * 0.02f) // Ajustado padding
                    .height(screenHeight * 0.05f), // Reducido altura
                colors = ButtonDefaults.textButtonColors(contentColor = Color.White),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Inicio",
                    lineHeight = 4.24.em,
                    style = TextStyle(fontSize = (screenWidth.value * 0.07).sp), // Reducido tamaño fuente
                    color = Color.White
                )
            }
            // Botón Registro (activo)
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = screenWidth * 0.09f, top = screenHeight * 0.01f) // Ajustado padding
                    .width(screenWidth * 0.38f)
                    .height(screenHeight * 0.07f), // Reducido altura
                shape = RoundedCornerShape(100.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xffb08d73))
            ) {}
            Button(
                onClick = { /* ya estamos en registro */ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = screenWidth * 0.16f, top = screenHeight * 0.02f) // Ajustado padding
                    .height(screenHeight * 0.05f), // Reducido altura
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                elevation = null,
                shape = RoundedCornerShape(100.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Registro",
                    lineHeight = 4.24.em,
                    style = TextStyle(fontSize = (screenWidth.value * 0.07).sp), // Reducido tamaño fuente
                    color = Color.Black
                )
            }
        }

        // Formulario de registro - OPTIMIZADO
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.84f)
                .fillMaxHeight(0.62f) // Aumentado para dar más espacio al formulario
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CREA TU CUENTA",
                    color = Color(0xff71390c).copy(alpha = 0.79f),
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = (screenWidth.value * 0.05).sp), // Reducido tamaño fuente
                    modifier = Modifier.padding(top = screenHeight * 0.015f) // Reducido padding
                )

                Spacer(Modifier.height(screenHeight * 0.015f)) // Reducido espaciado

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(screenHeight * 0.065f), // Altura fija más pequeña
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xff995d2d),
                        unfocusedBorderColor = Color(0xffdbbba6),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(Modifier.height(screenHeight * 0.015f)) // Reducido espaciado

                OutlinedTextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = { Text("Apellido", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(screenHeight * 0.065f), // Altura fija más pequeña
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xff995d2d),
                        unfocusedBorderColor = Color(0xffdbbba6),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(Modifier.height(screenHeight * 0.015f)) // Reducido espaciado

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(screenHeight * 0.065f), // Altura fija más pequeña
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xff995d2d),
                        unfocusedBorderColor = Color(0xffdbbba6),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(Modifier.height(screenHeight * 0.015f)) // Reducido espaciado

                var passwordVisible by remember { mutableStateOf(false) }
                val trailingIcon = if (passwordVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(screenHeight * 0.065f),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xff995d2d),
                        unfocusedBorderColor = Color(0xffdbbba6),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = trailingIcon,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = Color.Black // O el color que desees para el icono
                            )
                        }
                    }
                )

                Spacer(Modifier.height(screenHeight * 0.015f)) // Reducido espaciado

                var confirmarPasswordVisible by remember { mutableStateOf(false) }
                val trailingIconConfirm = if (confirmarPasswordVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }

                OutlinedTextField(
                    value = confirmarPassword,
                    onValueChange = { confirmarPassword = it },
                    label = { Text("Verificación", color = Color.Gray) }, // Etiqueta corregida
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(screenHeight * 0.065f),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xff995d2d),
                        unfocusedBorderColor = Color(0xffdbbba6),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    visualTransformation = if (confirmarPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmarPasswordVisible = !confirmarPasswordVisible }) {
                            Icon(
                                imageVector = trailingIconConfirm,
                                contentDescription = if (confirmarPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = Color.Black // O el color que desees para el icono
                            )
                        }
                    }
                )

                Spacer(Modifier.height(screenHeight * 0.06f)) // Espaciado antes del botón

                Button(
                    onClick = { /* Acción registrar */ },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(screenHeight * 0.065f), // Reducido ligeramente
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xffdbbba6),
                        contentColor = Color(0xff995d2d)
                    )
                ) {
                    Text(
                        text = "Registrar",
                        style = TextStyle(fontSize = (screenWidth.value * 0.055).sp) // Ajustado tamaño fuente
                    )
                }

                Spacer(Modifier.height(screenHeight * 0.01f)) // Pequeño espaciado al final
            }
        }
    }
}

@Composable
fun DarkThemeNoIconYes(
    modifier: Modifier = Modifier,
    label1901: String = ""
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth(0.92f)
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Person",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label1901,
            color = Color.Gray,
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(widthDp = 412, heightDp = 917, showBackground = true)
@Composable
private fun RegisterPreview() {
    Register()
}