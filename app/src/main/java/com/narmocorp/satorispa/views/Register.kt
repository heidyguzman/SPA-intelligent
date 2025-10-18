package com.narmocorp.satorispa.views

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

    // State for validation errors
    var nombreError by remember { mutableStateOf<String?>(null) }
    var apellidoError by remember { mutableStateOf<String?>(null) }
    var correoError by remember { mutableStateOf<String?>(null) }
    var contrasenaError by remember { mutableStateOf<String?>(null) }
    var confirmarContrasenaError by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // Consistent color palette
    val primaryBrandColor = Color(0xff995d2d)
    val secondaryBrandColor = Color(0xffdbbba6)
    val tertiaryBrandColor = Color(0xffb08d73)
    val textOnPrimaryBrand = Color.White
    val textOnSecondaryPlatform = Color(0xff71390c)
    val subtleTextColor = Color.Gray
    val pageBackgroundColor = Color.White
    val errorColor = MaterialTheme.colorScheme.error

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
        contrasenaError = when {
            contrasena.isBlank() -> "La contraseña no puede estar vacía"
            contrasena.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
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
            .background(pageBackgroundColor)
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
                .size(160.dp),
            contentScale = ContentScale.Fit
        )

        // Main content card
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.72f),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            colors = CardDefaults.cardColors(containerColor = pageBackgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding() // Makes the column content move up with the keyboard
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(52.dp + 48.dp)) // Space for selector

                Text(
                    text = "CREA TU CUENTA",
                    color = textOnSecondaryPlatform.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryBrandColor,
                    unfocusedBorderColor = secondaryBrandColor,
                    focusedTextColor = textOnSecondaryPlatform,
                    unfocusedTextColor = textOnSecondaryPlatform,
                    errorTextColor = textOnSecondaryPlatform,
                    cursorColor = primaryBrandColor,
                    focusedLabelColor = primaryBrandColor,
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
                            nombre = it
                            nombreError = if (it.isBlank()) "El nombre no puede estar vacío" else null
                        },
                        label = { Text("Nombre") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Nombre", tint = primaryBrandColor) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors,
                        singleLine = true,
                        isError = nombreError != null,
                        supportingText = { if (nombreError != null) Text(nombreError!!) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Right) })
                    )
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = {
                            apellido = it
                            apellidoError = if (it.isBlank()) "El apellido no puede estar vacío" else null
                        },
                        label = { Text("Apellido") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Apellido", tint = primaryBrandColor) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors,
                        singleLine = true,
                        isError = apellidoError != null,
                        supportingText = { if (apellidoError != null) Text(apellidoError!!) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Correo Electrónico", tint = primaryBrandColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors.copy(
                        focusedTextColor = textOnSecondaryPlatform,
                        unfocusedTextColor = textOnSecondaryPlatform
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
                            it.length < 6 -> "Debe tener al menos 6 caracteres"
                            else -> null
                        }
                        if (confirmarContrasena.isNotBlank()) {
                            confirmarContrasenaError = if (it != confirmarContrasena) "Las contraseñas no coinciden" else null
                        }
                    },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña", tint = primaryBrandColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors.copy(
                        focusedTextColor = textOnSecondaryPlatform,
                        unfocusedTextColor = textOnSecondaryPlatform
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                                tint = primaryBrandColor
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    singleLine = true,
                    isError = contrasenaError != null,
                    supportingText = { if (contrasenaError != null) Text(contrasenaError!!) }
                )

                Spacer(Modifier.height(4.dp))

                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = {
                        confirmarContrasena = it
                        confirmarContrasenaError = if (it != contrasena) "Las contraseñas no coinciden" else null
                    },
                    label = { Text("Confirmar Contraseña") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Confirmar Contraseña", tint = primaryBrandColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors.copy(
                        focusedTextColor = textOnSecondaryPlatform,
                        unfocusedTextColor = textOnSecondaryPlatform
                    ),
                    visualTransformation = if (confirmarPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmarPasswordVisible = !confirmarPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmarPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (confirmarPasswordVisible) "Ocultar" else "Mostrar",
                                tint = primaryBrandColor
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
                            // Diseño solamente: no hay lógica de red ni navegación
                            showMessage("Acción deshabilitada (solo diseño)")
                        } else {
                            showMessage("Completa los campos correctamente")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBrandColor,
                        contentColor = textOnPrimaryBrand
                    ),
                    enabled = true
                ) {
                    Text(
                        text = "Registrarme",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    )
                }
            }
        }

        // Login/Register Selector
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
            TextButton(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = textOnPrimaryBrand)
            ) {
                Text("Inicio", style = MaterialTheme.typography.titleSmall)
            }
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

        // Snackbar for showing messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}