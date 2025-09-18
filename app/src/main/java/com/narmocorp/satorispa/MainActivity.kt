package com.narmocorp.satorispa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.narmocorp.satorispa.ui.theme.SATORISPATheme
import com.narmocorp.satorispa.views.Login
import com.narmocorp.satorispa.views.Register
import com.narmocorp.satorispa.views.StartScreen




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SATORISPATheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "start") {
                    composable("start") {
                        StartScreen(
                            onLoginClick = { navController.navigate("login") },
                            onRegisterClick = { navController.navigate("register") }
                        )
                    }
                    composable("login") {
                        Login(label1901 = "Correo electrónico", navController = navController)
                    }
                    composable("register") {
                        Register(navController = navController)
                    }
                }
            }
        }
    }
}
