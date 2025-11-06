package com.narmocorp.satorispa.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class FaqItem(
    val category: String,
    val question: String,
    val answer: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyudaScreen(navController: NavController) {
    val faqList = listOf(
        FaqItem("Cuenta y acceso", "¿Cómo puedo registrarme o iniciar sesión?", "Regístrate desde la pantalla principal en “Crear cuenta” con tu nombre, correo y contraseña, o inicia sesión si ya tienes una cuenta."),
        FaqItem("Cuenta y acceso", "¿Olvidé mi contraseña, ¿qué hago?", "Selecciona la opción “¿Olvidaste tu contraseña?” en la pantalla de inicio de sesión. Recibirás un enlace para restablecerla en tu correo."),
        FaqItem("Citas y servicios", "¿Puedo cambiar mi cita una vez agendada?", "Sí. En el apartado “Mis citas”, selecciona la cita que deseas modificar y presiona “Reprogramar” o “Cancelar”. Recibirás una confirmación al instante."),
        FaqItem("Citas y servicios", "¿Qué servicios ofrece el spa?", "Puedes consultar todos los servicios disponibles en la sección “Catálogo de servicios”, con su descripción y precio."),
        FaqItem("Citas y servicios", "¿Recibiré una notificación antes de mi cita?", "Sí. La app te enviará un recordatorio con al menos 24 horas de anticipación antes de tu cita."),
        FaqItem("Pagos", "¿Qué métodos de pago aceptan?", "Aceptamos pagos con tarjeta de crédito o débito, transferencias y pagos en efectivo al llegar al spa."),
        FaqItem("Atención al cliente", "¿Cómo puedo contactar con atención al cliente?", "Puedes comunicarte con nosotros desde el menú “Ayuda” → 312 233 7959.\nTambién puedes escribirnos por WhatsApp o al correo rvuelvas@ucol.mx")
    )

    val groupedFaqs = faqList.groupBy { it.category }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ayuda y Soporte") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            groupedFaqs.forEach { (category, faqs) ->
                item {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(faqs) { faq ->
                    ExpandableFaqItem(faq = faq)
                }
            }
        }
    }
}

@Composable
fun ExpandableFaqItem(faq: FaqItem) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = faq.question,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Contraer" else "Expandir"
            )
        }
        if (expanded) {
            Text(
                text = faq.answer,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp, start = 16.dp)
            )
        }
    }
}
