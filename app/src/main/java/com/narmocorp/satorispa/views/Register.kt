package com.narmocorp.satorispa.views

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.narmocorp.satorispa.R
import com.narmocorp.satorispa.controller.RegistroController

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
    var isLoading by remember { mutableStateOf(false) }

    // State for validation errors
    var nombreError by remember { mutableStateOf<String?>(null) }
    var apellidoError by remember { mutableStateOf<String?>(null) }
    var correoError by remember { mutableStateOf<String?>(null) }
    var contrasenaError by remember { mutableStateOf<String?>(null) }
    var confirmarContrasenaError by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // 🎨 FUSIÓN CLAVE: USAR COLORES DEL TEMA (copia.kt)
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground // Color del texto principal
    val subtleTextColor = MaterialTheme.colorScheme.onSurfaceVariant // Usado para labels no enfocados
    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val errorColor = MaterialTheme.colorScheme.error
    val successColor = Color(0xFF4CAF50) // Mantener verde fijo para éxito

    fun showMessage(message: String) {
        scope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    fun validateFields(): Boolean {
        nombreError = if (nombre.isBlank()) "El nombre no puede estar vacío" else null
        apellidoError = if (apellido.isBlank()) "El apellido no puede estar vacío" else null
        correoError = when {
            correo.isBlank() -> "El correo no puede estar vacío"
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> "Ingresa un correo válido"
            else -> null
        }
        // Lógica de validación de contraseña CORRECTA (tomada de Register.kt)
        contrasenaError = when {
            contrasena.isBlank() -> "La contraseña no puede estar vacía"
            contrasena.length < 8 -> "La contraseña debe tener al menos 8 caracteres"
            !contrasena.any { it.isUpperCase() } -> "Debe incluir al menos una mayúscula"
            !contrasena.any { it.isDigit() } -> "Debe incluir al menos un número"
            else -> null
        }
        confirmarContrasenaError = when {
            confirmarContrasena.isBlank() -> "Confirma tu contraseña"
            contrasena != confirmarContrasena -> "Las contraseñas no coinciden"
            else -> null
        }
        return nombreError == null && apellidoError == null && correoError == null && contrasenaError == null && confirmarContrasenaError == null
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor) // Usar color del tema
    ) {
        val screenHeight = this.maxHeight

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
                .size(150.dp),
            contentScale = ContentScale.Fit
        )

        // Main content card
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.72f), // Se mantiene 0.72f
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor), // Usar color de tema
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(8.dp))

                // Selector "Inicio/Registro"
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(secondaryColor.copy(alpha = 0.9f)), // Usar color del tema
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { navController.navigate("login") },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSecondary) // Usar color del tema
                    ) {
                        Text("Inicio", style = MaterialTheme.typography.titleSmall)
                    }
                    TextButton(
                        onClick = { /* Already on Register */ },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .background(tertiaryColor, RoundedCornerShape(22.dp)), // Usar color del tema
                        shape = RoundedCornerShape(22.dp),
                        // FUSIÓN: Usamos onBackground (o onSurface) para el texto dentro del botón de registro para asegurar contraste en Modo Oscuro.
                        colors = ButtonDefaults.textButtonColors(contentColor = onBackgroundColor)
                    ) {
                        Text("Registro", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
                    }
                }
                Spacer(Modifier.height(25.dp)) // Espacio ajustado, como en copia.kt

                Text(
                    text = "CREA TU CUENTA",
                    color = onBackgroundColor.copy(alpha = 0.85f), // Usar color del tema
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Colores de TextField usando el tema
                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = secondaryColor,
                    focusedTextColor = onBackgroundColor,
                    unfocusedTextColor = onBackgroundColor,
                    errorTextColor = onBackgroundColor,
                    cursorColor = primaryColor,
                    focusedLabelColor = primaryColor,
                    unfocusedLabelColor = subtleTextColor,
                    errorBorderColor = errorColor,
                    errorLabelColor = errorColor
                )

                // Name and Last Name in a Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            // FUSIÓN: Lógica de filtro y límite de longitud de copia.kt
                            val filteredText = it.filter { char -> char.isLetter() || char.isWhitespace() }
                            if (filteredText.length <= 20) {
                                nombre = filteredText
                            }
                            nombreError = if (nombre.isBlank()) "El nombre no puede estar vacío" else null
                        },
                        label = { Text("Nombre") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Nombre", tint = primaryColor) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors,
                        singleLine = true,
                        isError = nombreError != null,
                        supportingText = { if (nombreError != null) Text(nombreError!!) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Right) })
                    )
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = {
                            // FUSIÓN: Lógica de filtro y límite de longitud de copia.kt
                            val filteredText = it.filter { char -> char.isLetter() || char.isWhitespace() }
                            if (filteredText.length <= 15) {
                                apellido = filteredText
                            }
                            apellidoError = if (apellido.isBlank()) "El apellido no puede estar vacío" else null
                        },
                        label = { Text("Apellido") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Apellido", tint = primaryColor) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors,
                        singleLine = true,
                        isError = apellidoError != null,
                        supportingText = { if (apellidoError != null) Text(apellidoError!!) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )
                }

                Spacer(Modifier.height(4.dp))

                OutlinedTextField(
                    value = correo,
                    onValueChange = {
                        correo = it
                        correoError = when {
                            it.isBlank() -> "El correo no puede estar vacío"
                            !Patterns.EMAIL_ADDRESS.matcher(it).matches() -> "Ingresa un correo válido"
                            else -> null
                        }
                    },
                    label = { Text("Correo Electrónico") },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Correo Electrónico", tint = primaryColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors.copy(
                        focusedTextColor = onBackgroundColor,
                        unfocusedTextColor = onBackgroundColor
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    singleLine = true,
                    isError = correoError != null,
                    supportingText = { if (correoError != null) Text(correoError!!) }
                )

                Spacer(Modifier.height(4.dp))

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = {
                        contrasena = it
                        contrasenaError = when {
                            it.isBlank() -> "La contraseña no puede estar vacía"
                            it.length < 8 -> "Debe tener al menos 8 caracteres"
                            !it.any { char -> char.isUpperCase() } -> "Debe incluir al menos una mayúscula"
                            !it.any { char -> char.isDigit() } -> "Debe incluir al menos un número"
                            else -> null
                        }
                        // FUSIÓN: Eliminar la lógica de "it.length <= 15" para no forzar el cambio en confirmarContrasena
                        if (confirmarContrasena.isNotBlank()) {
                            confirmarContrasenaError = if (it != confirmarContrasena) "Las contraseñas no coinciden" else null
                        }
                    },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña", tint = primaryColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors.copy(
                        focusedTextColor = onBackgroundColor,
                        unfocusedTextColor = onBackgroundColor
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                                tint = primaryColor
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    singleLine = true,
                    isError = contrasenaError != null,
                    supportingText = {

                        //Lógica para mostrar el mensaje
                        if (contrasena.isNotEmpty()) {
                            if (contrasenaError != null) {
                                // Muestra el error detallado usando el color del tema
                                Text(contrasenaError!!, color = errorColor)
                            } else {
                                // Muestra el mensaje de éxito en verde fijo
                                Text("Contraseña segura", color = successColor)
                            }
                        }
                    }
                )

                Spacer(Modifier.height(4.dp))

                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = {
                        confirmarContrasena = it
                        confirmarContrasenaError = if (it != contrasena) "Las contraseñas no coinciden" else null
                    },
                    label = { Text("Confirmar Contraseña") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Confirmar Contraseña", tint = primaryColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors.copy(
                        focusedTextColor = onBackgroundColor,
                        unfocusedTextColor = onBackgroundColor
                    ),
                    visualTransformation = if (confirmarPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmarPasswordVisible = !confirmarPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmarPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (confirmarPasswordVisible) "Ocultar" else "Mostrar",
                                tint = primaryColor
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    singleLine = true,
                    isError = confirmarContrasenaError != null,
                    supportingText = { if (confirmarContrasenaError != null) Text(confirmarContrasenaError!!) }
                )

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (validateFields()) {
                            isLoading = true
                            RegistroController.registerUser(nombre, apellido, correo, contrasena) { success, message ->
                                isLoading = false
                                if (success) {
                                    showMessage("Registro correcto")

                                    val usuarioId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid

                                    if (usuarioId != null) {
                                        scope.launch {
                                            try {
                                                val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                                                val notificacion = mapOf(
                                                    "titulo" to "¡Bienvenido a Satori SPA!",
                                                    "mensaje" to "Nos alegra tenerte aquí. Descubre nuestros servicios",
                                                    "tipo" to "bienvenida",
                                                    "fecha" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                                                    "leida" to false
                                                )

                                                db.collection("usuarios").document(usuarioId)
                                                    .collection("notificaciones")
                                                    .add(notificacion)
                                                    .addOnSuccessListener {
                                                        Log.d("Registro", "Notificación de bienvenida guardada")
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Log.e("Registro", "Error guardando notificación", e)
                                                    }
                                            } catch (e: Exception) {
                                                Log.e("Registro", "Exception", e)
                                            }
                                        }
                                    }

                                    scope.launch {
                                        kotlinx.coroutines.delay(1600)
                                        navController.navigate("login") {
                                            popUpTo("register") { inclusive = true }
                                        }
                                    }
                                } else {
                                    showMessage(message)
                                }
                            }
                        } else {
                            showMessage("Completa los campos correctamente")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor, // Usar color del tema
                        contentColor = onPrimaryColor // Usar color del tema
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = onPrimaryColor, // Usar color del tema
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Registrando...",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        )
                    } else {
                        Text(
                            text = "Registrarme",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        )
                    }
                }

                // FUSIÓN: Mantener el Spacer grande DENTRO del scroll para manejar el teclado
                Spacer(Modifier.height(100.dp))
            }
        }

        // Snackbar for showing messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}