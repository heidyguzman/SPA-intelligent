package com.narmocorp.satorispa

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narmocorp.satorispa.api.RetrofitClient
import com.narmocorp.satorispa.models.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.narmocorp.satorispa.ui.theme.SATORISPATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Estado para guardar los usuarios que vienen de la API
        val usuariosState = mutableStateListOf<Usuario>()

        // Llamada GET a tu API
        RetrofitClient.instance.getUsuarios().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (response.isSuccessful) {
                    val usuarios = response.body()
                    usuarios?.let {
                        usuariosState.addAll(it) // guardamos los usuarios en el estado
                        it.forEach { usuario ->
                            Log.d("API", "ID: ${usuario.id}, Nombre: ${usuario.nombre} ${usuario.apellido}, Correo: ${usuario.correo}")
                        }
                    }
                } else {
                    Log.e("API", "Error en respuesta: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                Log.e("API", "Error en conexión: ${t.message}")
            }
        })

        setContent {
            SATORISPATheme {
                UsuariosScreen(usuarios = usuariosState)
            }
        }
    }
}

@Composable
fun UsuariosScreen(usuarios: List<Usuario>) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (usuarios.isEmpty()) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Cargando usuarios...")
        } else {
            usuarios.forEach { usuario ->
                UsuarioCard(usuario = usuario)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun UsuarioCard(usuario: Usuario) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${usuario.nombre} ${usuario.apellido}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "ID: ${usuario.id}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Correo: ${usuario.correo}",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
            )
        }
    }
}
