package com.narmocorp.satorispa.views

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.narmocorp.satorispa.R
import com.narmocorp.satorispa.api.RetrofitClient
import com.narmocorp.satorispa.models.Usuario
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Register(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmarPasswordVisible by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Consistent color palette (same as Login)
    val primaryBrandColor = Color(0xff995d2d)
    val secondaryBrandColor = Color(0xffdbbba6)
    val tertiaryBrandColor = Color(0xffb08d73)
    val textOnPrimaryBrand = Color.White
    val textOnSecondaryPlatform = Color(0xff71390c)
    val subtleTextColor = Color.Gray
    val pageBackgroundColor = Color.White

    fun showMessage(message: String) {
        scope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(pageBackgroundColor)
    ) {
        val screenHeight = maxHeight

        // Background Image
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.44f),
            contentScale = ContentScale.Crop
        )

        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.05f)
                .size(160.dp),
            contentScale = ContentScale.Fit
        )

        // Main content card for registration form
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.72f), // Overlaps with top image and selector
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            colors = CardDefaults.cardColors(containerColor = pageBackgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp) // Symmetrical horizontal padding
                    .verticalScroll(rememberScrollState()), // Make form scrollable if content overflows
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Spacer to account for the "Login/Register Selector"
                Spacer(Modifier.height(52.dp + 24.dp)) // Space for selector + breathing room

                Text(
                    text = "CREA TU CUENTA",
                    color = textOnSecondaryPlatform.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                val textFieldModifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp) // Reduced bottom padding for denser form

                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryBrandColor,
                    unfocusedBorderColor = secondaryBrandColor,
                    focusedTextColor = textOnSecondaryPlatform,
                    unfocusedTextColor = textOnSecondaryPlatform,
                    cursorColor = primaryBrandColor,
                    focusedLabelColor = primaryBrandColor,
                    unfocusedLabelColor = subtleTextColor
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = textFieldModifier,
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors,
                    singleLine = true
                )

                OutlinedTextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = { Text("Apellido") },
                    modifier = textFieldModifier,
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors,
                    singleLine = true
                )

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo Electrónico") },
                    modifier = textFieldModifier,
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña") },
                    modifier = textFieldModifier,
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description, tint = primaryBrandColor)
                        }
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = { confirmarContrasena = it },
                    label = { Text("Confirmar Contraseña") },
                    modifier = textFieldModifier.padding(bottom = 20.dp), // More space before button
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors,
                    visualTransformation = if (confirmarPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (confirmarPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description = if (confirmarPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        IconButton(onClick = { confirmarPasswordVisible = !confirmarPasswordVisible }) {
                            Icon(imageVector = image, description, tint = primaryBrandColor)
                        }
                    },
                    singleLine = true
                )

                Button(
                    onClick = {
                        when {
                            nombre.isBlank() -> showMessage("Por favor, ingresa tu nombre")
                            apellido.isBlank() -> showMessage("Por favor, ingresa tu apellido")
                            correo.isBlank() -> showMessage("Por favor, ingresa tu correo")
                            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> showMessage("Ingresa un correo válido")
                            contrasena.isBlank() -> showMessage("Por favor, ingresa tu contraseña")
                            contrasena.length < 6 -> showMessage("La contraseña debe tener al menos 6 caracteres")
                            confirmarContrasena.isBlank() -> showMessage("Por favor, confirma tu contraseña")
                            contrasena != confirmarContrasena -> showMessage("Las contraseñas no coinciden")
                            else -> {
                                val newUser = Usuario(
                                    id = 0, // Assuming ID is auto-generated by backend
                                    nombre = nombre,
                                    apellido = apellido,
                                    correo = correo,
                                    contrasena = contrasena
                                )
                                RetrofitClient.instance.registerUser(newUser).enqueue(object : Callback<Usuario> {
                                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                                        if (response.isSuccessful) {
                                            showMessage("¡Registro exitoso!")
                                            navController.navigate("login") {
                                                popUpTo("register") { inclusive = true }
                                                launchSingleTop = true
                                            }
                                        } else {
                                            val errorMessage = when (response.code()) {
                                                409 -> "Este correo ya está registrado"
                                                400 -> "Datos inválidos, revisa la información"
                                                500 -> "Error del servidor, intenta más tarde"
                                                else -> "Error en el registro (Cód: ${response.code()})"
                                            }
                                            showMessage(errorMessage)
                                        }
                                    }

                                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                                        showMessage("Error de conexión: ${t.message}")
                                    }
                                })
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBrandColor,
                        contentColor = textOnPrimaryBrand
                    )
                ) {
                    Text(
                        text = "Registrar",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    )
                }
                Spacer(Modifier.height(16.dp)) // Space at the bottom of the scrollable area
            }
        }

        // "Login" / "Register" Selector
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.32f)
                .fillMaxWidth(0.82f)
                .height(52.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(secondaryBrandColor.copy(alpha = 0.9f)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Login Button
            TextButton(
                onClick = { navController.navigate("login") { popUpTo("register") { inclusive = true } } },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = textOnPrimaryBrand)
            ) {
                Text("Inicio", style = MaterialTheme.typography.titleSmall)
            }

            // Register Button (Active)
            TextButton(
                onClick = { /* Already on Register */ },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .background(tertiaryBrandColor, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
            ) {
                Text("Registro", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
            }
        }

        // SnackbarHost for displaying messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        )
    }
}
