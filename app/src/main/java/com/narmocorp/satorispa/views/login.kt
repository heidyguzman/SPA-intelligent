package com.narmocorp.satorispa.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.narmocorp.satorispa.R
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun Login(
    modifier: Modifier = Modifier,
    label1901: String,
    onLogin: (String, String) -> Unit // Nuevo parámetro para manejar el login
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        // Fondo superior con imagen
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "fondo",
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.44f),
            contentScale = ContentScale.Crop
        )
        // Logo con imagen
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.03f)
                .size(screenWidth * 0.58f, screenHeight * 0.21f),
            contentScale = ContentScale.Crop
        )
        // Card principal
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.98f)
                .fillMaxHeight(0.72f),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {}
        // Tabs de Inicio/Registro
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.32f)
                .fillMaxWidth(0.82f)
                .height(screenHeight * 0.11f)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RoundedCornerShape(100.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xffdbbba6).copy(alpha = 0.86f))
            ) {}
            // Botón "Inicio" con Card de fondo
            Card(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = screenWidth * 0.09f, top = screenHeight * 0.015f)
                    .width(screenWidth * 0.32f)
                    .height(screenHeight * 0.08f),
                shape = RoundedCornerShape(100.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xffb08d73))
            ) {}
            Button(
                onClick = { /* Acción para ir a Login */ },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = screenWidth * 0.16f, top = screenHeight * 0.03f)
                    .height(screenHeight * 0.06f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                elevation = null,
                shape = RoundedCornerShape(100.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Inicio",
                    lineHeight = 4.24.em,
                    style = TextStyle(fontSize = (screenWidth.value * 0.08).sp),
                    color = Color.Black
                )
            }
            // Botón "Registro"
            TextButton(
                onClick = { /* Acción para ir a Registro */ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = screenWidth * 0.08f, top = screenHeight * 0.03f)
                    .height(screenHeight * 0.06f),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Registro",
                    lineHeight = 4.24.em,
                    style = TextStyle(fontSize = (screenWidth.value * 0.08).sp),
                    color = Color.White
                )
            }
        }
        // Formulario
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.84f)
                .fillMaxHeight(0.48f)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "SATORI SPA TE DA LA BIENVENIDA",
                    color = Color(0xff71390c).copy(alpha = 0.79f),
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = (screenWidth.value * 0.055).sp),
                    modifier = Modifier.padding(top = screenHeight * 0.025f)
                )

                Spacer(Modifier.height(screenHeight * 0.035f))
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text(text = label1901, color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(0.92f),

                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xff995d2d),
                        unfocusedBorderColor = Color(0xffdbbba6),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(Modifier.height(screenHeight * 0.02f))

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text(text = "Contraseña", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(0.92f),
                    shape = RoundedCornerShape(16.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xff995d2d),
                        unfocusedBorderColor = Color(0xffdbbba6),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(Modifier.height(screenHeight * 0.02f))

                Button(
                    onClick = { onLogin(correo, contrasena) },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(screenHeight * 0.08f),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xffdbbba6),
                        contentColor = Color(0xff995d2d)
                    )
                ) {
                    Text(
                        text = "Iniciar",
                        style = TextStyle(fontSize = (screenWidth.value * 0.06).sp)
                    )
                }

                Spacer(Modifier.height(screenHeight * 0.015f)) // Reducido de 0.025f

                TextButton(
                    onClick = { /* Acción para olvidar contraseña */ },
                    modifier = Modifier
                        .fillMaxWidth(0.92f) // Más ancho
                        .height(screenHeight * 0.07f) // Más alto
                ) {
                    Text(
                        text = "Olvide contraseña?",
                        color = Color(0xff995d2d),
                        textDecoration = TextDecoration.Underline,
                        style = TextStyle(fontSize = (screenWidth.value * 0.05).sp), // Texto más grande
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.weight(1f)) // Este spacer debería ayudar a empujar el contenido hacia arriba si hay espacio, o permitir que se encoja.
            }
        }
    }
}
