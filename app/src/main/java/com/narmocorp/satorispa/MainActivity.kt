package com.narmocorp.satorispa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.narmocorp.satorispa.ui.theme.SatoriSPATheme
import com.narmocorp.satorispa.views.ServicesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SatoriSPATheme {
                ServicesScreen()
            }
        }
    }
}
