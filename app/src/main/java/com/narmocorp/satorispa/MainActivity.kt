package com.narmocorp.satorispa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.narmocorp.satorispa.ui.theme.SatoriSPATheme
import com.narmocorp.satorispa.views.ServicesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SatoriSPATheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "start") {
                    composable("start") {
                        StartScreen(
                            onServicesClick = { navController.navigate("services") },
                            onRegisterClick = { /* TODO: navigate to register if needed */ }
                        )
                    }

                    composable("services") {
                        ServicesScreen()
                    }
                }
            }
        }
    }
}
