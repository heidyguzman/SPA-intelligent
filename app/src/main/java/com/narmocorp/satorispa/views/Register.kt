package com.narmocorp.satorispa.views

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.narmocorp.satorispa.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    var terminosAceptados by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var apellidoError by remember { mutableStateOf<String?>(null) }
    var correoError by remember { mutableStateOf<String?>(null) }
    var contrasenaError by remember { mutableStateOf<String?>(null) }
    var confirmarContrasenaError by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val surfaceColor = MaterialTheme.colorScheme.surface
    val errorColor = MaterialTheme.colorScheme.error
    val successColor = Color(0xFF4CAF50)
    val titleColor = if (isSystemInDarkTheme()) Color.White else Color(0xff995d2d)

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
            .background(MaterialTheme.colorScheme.background)
    ) {
        val screenHeight = this.maxHeight

        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.44f),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.05f)
                .size(150.dp),
            contentScale = ContentScale.Fit
        )

        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.72f),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor),
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(secondaryColor.copy(alpha = 0.9f)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { navController.navigate("login") },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSecondary)
                    ) {
                        Text("Inicio", style = MaterialTheme.typography.titleSmall)
                    }
                    TextButton(
                        onClick = { /* Already on Register */ },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .background(tertiaryColor, RoundedCornerShape(22.dp)),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.textButtonColors(contentColor = onBackgroundColor)
                    ) {
                        Text("Registro", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
                    }
                }
                Spacer(Modifier.height(25.dp))

                Text(
                    text = "CREA TU CUENTA",
                    color = titleColor,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = secondaryColor,
                    focusedLabelColor = primaryColor,
                    cursorColor = primaryColor
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
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
                    colors = textFieldColors,
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
                        if (confirmarContrasena.isNotBlank()) {
                            confirmarContrasenaError = if (it != confirmarContrasena) "Las contraseñas no coinciden" else null
                        }
                    },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña", tint = primaryColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors,
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
                        if (contrasena.isNotEmpty()) {
                            if (contrasenaError != null) {
                                Text(contrasenaError!!, color = errorColor)
                            } else {
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
                    colors = textFieldColors,
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
                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = terminosAceptados,
                        onCheckedChange = { terminosAceptados = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = primaryColor,
                            uncheckedColor = onBackgroundColor,
                            checkmarkColor = onPrimaryColor
                        )
                    )
                    val annotatedString = buildAnnotatedString {
                        append("Acepto los ")
                        pushStringAnnotation(tag = "TERMS", annotation = "terms")
                        withStyle(style = SpanStyle(color = primaryColor, fontWeight = FontWeight.Bold)) {
                            append("Términos y Condiciones")
                        }
                        pop()
                    }
                    ClickableText(
                        text = annotatedString,
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                                .firstOrNull()?.let {
                                    showTermsDialog = true
                                }
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(color = onBackgroundColor)
                    )
                }

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (validateFields()) {
                            isLoading = true
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, contrasena)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val user = FirebaseAuth.getInstance().currentUser
                                        val db = FirebaseFirestore.getInstance()
                                        val userData = hashMapOf(
                                            "nombre" to nombre,
                                            "apellido" to apellido,
                                            "correo" to correo,
                                            "rol" to "cliente",
                                            "imagenUrl" to ""
                                        )
                                        user?.let {
                                            db.collection("usuarios").document(it.uid)
                                                .set(userData)
                                                .addOnSuccessListener {
                                                    isLoading = false
                                                    showMessage("Registro exitoso. ¡Bienvenido!")
                                                    scope.launch {
                                                        delay(1600)
                                                        navController.navigate("login") {
                                                            popUpTo("register") { inclusive = true }
                                                        }
                                                    }
                                                }
                                                .addOnFailureListener { e ->
                                                    isLoading = false
                                                    showMessage("Error al guardar datos: ${e.message}")
                                                }
                                        }
                                    } else {
                                        isLoading = false
                                        val exception = task.exception
                                        val errorMessage = when (exception) {
                                            is FirebaseAuthUserCollisionException -> "El correo electrónico ya está en uso por otra cuenta."
                                            else -> "Error en el registro: ${exception?.message}"
                                        }
                                        showMessage(errorMessage)
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
                        containerColor = primaryColor,
                        contentColor = onPrimaryColor
                    ),
                    enabled = !isLoading && terminosAceptados,
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = onPrimaryColor,
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

                Spacer(Modifier.height(100.dp))
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }

    if (showTermsDialog) {
        TermsAndConditionsDialog(onDismiss = { showTermsDialog = false })
    }
}

@Composable
fun TermsAndConditionsDialog(onDismiss: () -> Unit) {
    val termsText = """
Última actualización: Octubre 2025

1. Aceptación de los Términos
Al acceder y utilizar la aplicación Satori SPA, usted acepta estar sujeto a estos Términos y Condiciones. Si no está de acuerdo con alguna parte de estos términos, no debe utilizar nuestra aplicación.

2. Uso de la Aplicación
Satori SPA es una plataforma que conecta clientes con terapeutas profesionales para servicios de spa y bienestar. Los usuarios se comprometen a:
• Proporcionar información precisa y actualizada
• Mantener la confidencialidad de su cuenta
• No utilizar la aplicación para fines ilegales
• Respetar a otros usuarios y terapeutas

3. Registro y Cuenta
Para utilizar ciertos servicios, debe crear una cuenta. Usted es responsable de:
• Mantener su contraseña segura
• Todas las actividades realizadas bajo su cuenta
• Notificar inmediatamente cualquier uso no autorizado

4. Servicios y Reservas
Las reservas de servicios están sujetas a:
• Disponibilidad del terapeuta
• Políticas de cancelación específicas
• Confirmación previa a la cita

5. Acceso mediante NFC 
El acceso mediante tarjetas NFC:
• Permite validar accesos dentro del spa.
• El usuario es responsable del uso adecuado de su tarjeta NFC asociado a su cuenta.

7. Responsabilidades
Satori SPA actúa como intermediario entre clientes y terapeutas. No somos responsables de:
• La calidad de los servicios proporcionados
• Lesiones o daños durante el tratamiento
• Cancelaciones por parte del terapeuta
• Resultados específicos de los tratamientos

8. Propiedad Intelectual
Todo el contenido de la aplicación, incluyendo textos, gráficos, logos e imágenes, es propiedad de Satori SPA y está protegido por leyes de propiedad intelectual.

9. Modificaciones
Nos reservamos el derecho de modificar estos términos en cualquier momento. Los cambios entrarán en vigencia inmediatamente después de su publicación en la aplicación.

10. Terminación
Podemos suspender o terminar su acceso a la aplicación por:
• Violación de estos términos
• Comportamiento inapropiado
• Fraude o actividades sospechosas
• A nuestra discreción

11. Ley Aplicable
Estos términos se rigen por las leyes de México. Cualquier disputa será resuelta en los tribunales de Manzanillo, México.
"""

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Términos y Condiciones") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(text = termsText, style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}