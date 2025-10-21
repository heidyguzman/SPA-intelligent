package com.narmocorp.satorispa.views

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ForgotPasswordFlow(navController: NavController) {
    val nestedNavController = rememberNavController()
    NavHost(nestedNavController, startDestination = "send_email") {
        composable("send_email") {
            SendPasswordResetEmailScreen(nestedNavController, navController)
        }
        composable("enter_code") {
            EnterResetCodeScreen(nestedNavController, navController)
        }
        composable("reset_password") {
            ResetPasswordScreen(nestedNavController, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendPasswordResetEmailScreen(nestedNavController: NavController, rootNavController: NavController) {
    var email by remember { mutableStateOf("") }
    val primaryBrandColor = Color(0xff995d2d)
    val pageBackgroundColor = Color.White
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recuperar Contraseña") },
                navigationIcon = {
                    IconButton(onClick = { rootNavController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = pageBackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Recuperar contraseña",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth(),
                 shape = RoundedCornerShape(16.dp),
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Correo de recuperación enviado.", Toast.LENGTH_SHORT).show()
                                    // The user will reset the password via a link in the email.
                                    // You might want to navigate back to the login screen after this.
                                     rootNavController.popBackStack()
                                } else {
                                    Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Por favor, introduce tu correo.", Toast.LENGTH_SHORT).show()
                    }
                },
                 modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBrandColor)
            ) {
                Text("Enviar", color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterResetCodeScreen(nestedNavController: NavController, rootNavController: NavController) {
    var code by remember { mutableStateOf("") }
    val primaryBrandColor = Color(0xff995d2d)
    val pageBackgroundColor = Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recuperar Contraseña") },
                navigationIcon = {
                    IconButton(onClick = { nestedNavController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = pageBackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
             Text(
                text = "Recuperar contraseña",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("Código") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    // This flow is not standard with Firebase password reset.
                    // The standard flow uses a link in the email.
                    // Keeping the UI for now, but skipping to the final step for demonstration.
                    nestedNavController.navigate("reset_password")
                },
                 modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBrandColor)
            ) {
                Text("Enviar", color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(nestedNavController: NavController, rootNavController: NavController) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val primaryBrandColor = Color(0xff995d2d)
    val pageBackgroundColor = Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recuperar Contraseña") },
                navigationIcon = {
                    IconButton(onClick = { nestedNavController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = pageBackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
             Text(
                text = "Recuperar contraseña",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                 shape = RoundedCornerShape(16.dp),
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Verificación") },
                modifier = Modifier.fillMaxWidth(),
                 shape = RoundedCornerShape(16.dp),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    // This part of the flow is handled by Firebase through the email link.
                    // Navigating back to login for now.
                    rootNavController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                 modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBrandColor)
            ) {
                Text("Cambiar", color = Color.White)
            }
        }
    }
}
