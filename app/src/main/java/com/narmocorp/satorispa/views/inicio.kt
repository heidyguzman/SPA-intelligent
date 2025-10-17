package com.narmocorp.satorispa

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narmocorp.satorispa.R

@Composable
fun StartScreen(onServicesClick: () -> Unit, onRegisterClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Satori Spa",
                modifier = Modifier
                    .width(200.dp)
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mensaje de bienvenida
            Text(
                text = "Bienvenido a Satori Spa",
                color = Color(0xFF986A48),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            // Este spacer empuja el contenido debajo de él hacia la parte inferior de la pantalla
            Spacer(modifier = Modifier.weight(1f))

            // Botón principal de servicios
            Button(
                onClick = onServicesClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF986A48))
            ) {
                Text(
                    text = "Explorar servicios",
                    color = Color.White, // Texto blanco para mejor contraste
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para registrarse (usa onRegisterClick para evitar advertencia de parámetro no usado)
            OutlinedButton(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(30.dp),
                border = BorderStroke(1.dp, Color(0xFF986A48)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF986A48))
            ) {
                Text(text = "Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    StartScreen(onServicesClick = {}, onRegisterClick = {})
}