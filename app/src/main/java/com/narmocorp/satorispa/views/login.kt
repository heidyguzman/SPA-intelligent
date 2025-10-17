package com.narmocorp.satorispa.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
// It's good practice to import Material 3 components specifically
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
    label1901: String, // Suggest renaming to something like "emailLabel"
    onLogin: (String, String, Boolean) -> Unit,
    navController: NavController
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var keepSession by remember { mutableStateOf(false) }

    // Consider defining these in your MaterialTheme.colorScheme
    val primaryBrandColor = Color(0xff995d2d)
    val secondaryBrandColor = Color(0xffdbbba6) // Used for backgrounds/accents
    val tertiaryBrandColor = Color(0xffb08d73)  // Used for selected tab/active elements
    val textOnPrimaryBrand = Color.White
    val textOnSecondaryPlatform = Color(0xff71390c) // For titles or important text on lighter backgrounds
    val subtleTextColor = Color.Gray
    val pageBackgroundColor = Color.White

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(pageBackgroundColor)
            .imePadding() // Adjusts for the keyboard
    ) {
        val screenHeight = maxHeight
        // val screenWidth = maxWidth // Not directly used in this version, relying more on fillMaxWidth and fixed dp

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
                .padding(top = screenHeight * 0.05f) // Give a bit more space from top
                .size(160.dp), // Consistent logo size
            contentScale = ContentScale.Fit
        )

        // Main content card, positioned from the bottom
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.72f), // Overlaps with top image and "tab selector"
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp), // Only top corners rounded
            colors = CardDefaults.cardColors(containerColor = pageBackgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 16.dp), // Symmetrical horizontal padding
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Spacer to account for the "Login/Register Selector" that will be placed on top of this card.
                Spacer(Modifier.height(52.dp + 24.dp)) // Space for selector + some breathing room

                Text(
                    text = "SATORI SPA TE DA LA BIENVENIDA",
                    color = textOnSecondaryPlatform.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall, // More prominent title
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text(text = label1901, color = subtleTextColor) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = "Icono de Correo",
                            tint = primaryBrandColor
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBrandColor,
                        unfocusedBorderColor = secondaryBrandColor,
                        focusedTextColor = textOnSecondaryPlatform, // Updated text color
                        unfocusedTextColor = textOnSecondaryPlatform, // Updated text color
                        cursorColor = primaryBrandColor
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text(text = "Contraseña", color = subtleTextColor) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Icono de Contraseña",
                            tint = primaryBrandColor
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
                            Icon(imageVector  = image, contentDescription = description, tint = primaryBrandColor)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBrandColor,
                        unfocusedBorderColor = secondaryBrandColor,
                        focusedTextColor = textOnSecondaryPlatform, // Updated text color
                        unfocusedTextColor = textOnSecondaryPlatform, // Updated text color
                        cursorColor = primaryBrandColor
                    ),
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
                        color = subtleTextColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Switch(
                        checked = keepSession,
                        onCheckedChange = { keepSession = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = primaryBrandColor,
                            checkedTrackColor = tertiaryBrandColor,
                            uncheckedThumbColor = subtleTextColor,
                            uncheckedTrackColor = secondaryBrandColor
                        )
                    )
                }

                Button(
                    onClick = { onLogin(correo, contrasena, keepSession) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp), // Pill shape
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBrandColor, // Stronger call to action
                        contentColor = textOnPrimaryBrand   // Ensure contrast
                    )
                ) {
                    Text(
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    )
                }

                Spacer(Modifier.height(16.dp))

                TextButton(
                    onClick = { /* TODO: Implement forgot password */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = primaryBrandColor,
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // "Login" / "Register" Selector - Positioned like the original "tabs"
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter) // Align in the parent BoxWithConstraints
                .padding(top = screenHeight * 0.32f) // Original positioning
                .fillMaxWidth(0.82f) // Original width
                .height(52.dp)
                .clip(RoundedCornerShape(26.dp)) // pill shape for the row
                .background(secondaryBrandColor.copy(alpha = 0.9f)), // Background for the whole selector
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Login Button (Active)
            TextButton(
                onClick = { /* Already on Login */ },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp) // Padding to create inner selection effect
                    .background(tertiaryBrandColor, RoundedCornerShape(22.dp)), // Selected background with rounding
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Black) // Dark text on selected tab
            ) {
                Text("Inicio", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
            }

            // Register Button
            TextButton(
                onClick = { navController.navigate("register") },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp), // Consistent padding
                shape = RoundedCornerShape(22.dp), // Consistent rounding
                colors = ButtonDefaults.textButtonColors(contentColor = textOnPrimaryBrand) // Ensure this contrasts with secondaryBrandColor
            ) {
                Text("Registro", style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}
