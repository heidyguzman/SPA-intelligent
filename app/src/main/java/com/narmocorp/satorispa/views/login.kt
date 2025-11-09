package com.narmocorp.satorispa.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.narmocorp.satorispa.R

@Composable
fun Login(
    modifier: Modifier = Modifier,
    emailLabel: String,
    onLogin: (String, String, Boolean) -> Unit, // Se mantiene la firma correcta
    navController: NavController
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var keepSession by remember { mutableStateOf(false) }

    // GESTIÓN DE COLORES
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val subtleTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    // -------------------------------------------------------------

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor) // Color del tema
            .imePadding()
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
                .fillMaxHeight(0.65f),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor), // Color del tema
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // "Login" / "Register" Selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.82f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(secondaryColor.copy(alpha = 0.9f)), // Color del tema
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Login Button (Active)
                    TextButton(
                        onClick = { /* Already on Login */ },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .background(tertiaryColor, RoundedCornerShape(22.dp)), // Color del tema
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onTertiary) // Color del tema
                    ) {
                        Text("Inicio", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
                    }

                    // Register Button
                    TextButton(
                        onClick = { navController.navigate("register") },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSecondary) // Color del tema
                    ) {
                        Text("Registro", style = MaterialTheme.typography.titleSmall)
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "SATORI SPA LE DA LA BIENVENIDA",
                    color = onBackgroundColor.copy(alpha = 0.85f), // Color del tema
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // 🎨 TextField Colors: Usando colores del tema
                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = secondaryColor,
                    focusedTextColor = onBackgroundColor, // Texto adaptativo
                    unfocusedTextColor = onBackgroundColor, // Texto adaptativo
                    cursorColor = primaryColor
                )

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text(text = emailLabel, color = subtleTextColor) }, // Color del tema
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = "Icono de Correo",
                            tint = primaryColor
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors,
                    singleLine = true
                )

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text(text = "Contraseña", color = subtleTextColor) }, // Color del tema
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Icono de Contraseña",
                            tint = primaryColor
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(
                                imageVector  = image,
                                contentDescription = description,
                                tint = primaryColor
                            )
                        }
                    },
                    colors = textFieldColors,
                    singleLine = true
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Mantener sesión iniciada",
                        color = subtleTextColor, // Color del tema
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Switch(
                        checked = keepSession,
                        onCheckedChange = { keepSession = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = primaryColor,
                            checkedTrackColor = tertiaryColor, // Color del tema
                            uncheckedThumbColor = subtleTextColor, // Color del tema
                            uncheckedTrackColor = secondaryColor // Color del tema
                        )
                    )
                }

                Button(
                    onClick = { onLogin(correo, contrasena, keepSession) }, // Uso correcto de keepSession
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor, // Color del tema
                        contentColor = onPrimaryColor // Color del tema
                    )
                ) {
                    Text(
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    )
                }

                Spacer(Modifier.height(16.dp))

                TextButton(
                    onClick = { navController.navigate("forgot_password") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = primaryColor,
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
