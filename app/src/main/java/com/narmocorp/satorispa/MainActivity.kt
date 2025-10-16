package com.narmocorp.satorispa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.narmocorp.satorispa.ui.theme.SatoriSPATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = Firebase.firestore

        db.collection("usuarios")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    println("${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }
}
