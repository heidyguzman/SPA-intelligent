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
import com.narmocorp.satorispa.models.Usuario
import com.narmocorp.satorispa.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.util.Patterns


@Composable
fun Register(modifier: Modifier = Modifier, navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    // Variables para los mensajes
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Función para mostrar mensajes
    fun showMessage(message: String) {
        scope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

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
                onClick = { navController.navigate("login") },
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
                    label = { Text("Nombre", color = Color.Gray, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .heightIn(min = 56.dp), // Usar heightIn en lugar de height fijo
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xff995d2d),
                        unfocusedBorderColor = Color(0xffdbbba6),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color(0xff995d2d) // Color del cursor
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp // Mejor espaciado de línea
                    ),
                    singleLine = true, // Una sola línea
                    maxLines = 1 // Máximo una línea
                )

                Spacer(Modifier.height(screenHeight * 0.015f)) // Reducido espaciado

                OutlinedTextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = { Text("Apellido", color = Color.Gray, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .heightIn(min = 56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xff995d2d),
                        unfocusedBorderColor = Color(0xffdbbba6),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color(0xff995d2d)
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp
                    ),
                    singleLine = true,
                    maxLines = 1
                )

                Spacer(Modifier.height(screenHeight * 0.015f)) // Reducido espaciado

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo", color = Color.Gray, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .heightIn(min = 56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xff995d2d),
                        unfocusedBorderColor = Color(0xffdbbba6),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color(0xff995d2d)
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp
                    ),
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email // Teclado optimizado para email
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
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña", color = Color.Gray, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .heightIn(min = 56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xff995d2d),
                        unfocusedBorderColor = Color(0xffdbbba6),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color(0xff995d2d)
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = trailingIcon,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = Color(0xff995d2d) // Color del icono
                            )
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp
                    ),
                    singleLine = true,
                    maxLines = 1
                )

                Spacer(Modifier.height(screenHeight * 0.014f)) // Reducido espaciado

                var confirmarPasswordVisible by remember { mutableStateOf(false) }
                val trailingIconConfirm = if (confirmarPasswordVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }

                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = { confirmarContrasena = it },
                    label = { Text("Verificación", color = Color.Gray, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .heightIn(min = 56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xff995d2d),
                        unfocusedBorderColor = Color(0xffdbbba6),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color(0xff995d2d)
                    ),
                    visualTransformation = if (confirmarPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmarPasswordVisible = !confirmarPasswordVisible }) {
                            Icon(
                                imageVector = trailingIconConfirm,
                                contentDescription = if (confirmarPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = Color(0xff995d2d)
                            )
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp
                    ),
                    singleLine = true,
                    maxLines = 1
                )

                Spacer(Modifier.height(screenHeight * 0.02f)) // Espaciado antes del botón

                Button(
                    onClick = {
                        // Lógica de validación con mensajes
                        when {
                            nombre.isBlank() -> showMessage("Por favor, ingresa tu nombre")
                            apellido.isBlank() -> showMessage("Por favor, ingresa tu apellido")
                            correo.isBlank() -> showMessage("Por favor, ingresa tu correo")
                            contrasena.isBlank() -> showMessage("Por favor, ingresa tu contraseña")
                            confirmarContrasena.isBlank() -> showMessage("Por favor, confirma tu contraseña")
                            contrasena != confirmarContrasena -> showMessage("Las contraseñas no coinciden")
                            contrasena.length < 6 -> showMessage("La contraseña debe tener al menos 6 caracteres")
                            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> showMessage("Ingresa un correo válido")
                            else -> {
                                // Creación del objeto Usuario
                                val newUser = Usuario(
                                    id = 0,
                                    nombre = nombre,
                                    apellido = apellido,
                                    correo = correo,
                                    contrasena = contrasena
                                )

                                // Llamada a la API
                                RetrofitClient.instance.registerUser(newUser).enqueue(object : Callback<Usuario> {
                                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                                        if (response.isSuccessful) {
                                            // ✅ REGISTRO EXITOSO - IR DIRECTO AL LOGIN
                                            navController.navigate("login") {
                                                popUpTo("register") { inclusive = true }
                                                launchSingleTop = true
                                            }
                                        } else {
                                            val errorMessage = when (response.code()) {
                                                409 -> "Este correo ya está registrado"
                                                400 -> "Datos inválidos, revisa la información"
                                                500 -> "Error del servidor, intenta más tarde"
                                                else -> "Error en el registro"
                                            }
                                            showMessage(errorMessage)
                                        }
                                    }

                                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                                        showMessage("Sin conexión a internet, revisa tu conexión")
                                    }
                                })
                            }
                        }
                    },
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

        // SnackbarHost para mostrar mensajes
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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