package com.narmocorp.satorispa.views

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
        FaqItem("Cuenta y acceso", "¿Cómo puedo registrarme o iniciar sesión?", "Puedes registrarte fácilmente desde la pantalla principal seleccionando “Crear cuenta”. Solo necesitas tu nombre, correo electrónico y una contraseña.\nSi ya tienes una cuenta, selecciona “Iniciar sesión” e ingresa tus datos."),
        FaqItem("Cuenta y acceso", "¿Olvidé mi contraseña, ¿qué hago?", "Selecciona la opción “¿Olvidaste tu contraseña?” en la pantalla de inicio de sesión. Recibirás un enlace para restablecerla en tu correo."),
        FaqItem("Citas y servicios", "¿Puedo cambiar mi cita una vez agendada?", "Sí. En el apartado “Mis citas”, selecciona la cita que deseas modificar y presiona “Reprogramar” o “Cancelar”. Recibirás una confirmación al instante."),
        FaqItem("Citas y servicios", "¿Qué servicios ofrece el spa?", "Puedes consultar todos los servicios disponibles en la sección “Catálogo de servicios”, con su descripción, duración y precio."),
        FaqItem("Citas y servicios", "¿Recibiré una notificación antes de mi cita?", "Sí. La app te enviará un recordatorio con al menos 24 horas de anticipación y otro 1 hora antes de tu cita."),
        FaqItem("Pagos", "¿Qué métodos de pago aceptan?", "Aceptamos pagos con tarjeta de crédito o débito, transferencias y pagos en efectivo al llegar al spa."),
        FaqItem("Pagos", "¿Puedo solicitar factura?", "Sí. Después de tu cita, en el apartado “Historial de servicios” puedes solicitar la factura correspondiente."),
        FaqItem("Atención al cliente", "¿Cómo puedo contactar con atención al cliente?", "Puedes comunicarte con nosotros desde el menú “Ayuda” → “Contacto”.\nTambién puedes escribirnos por WhatsApp o al correo soporte@tuspa.com.")
    )

    val groupedFaqs = faqList.groupBy { it.category }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ayuda y Soporte", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            groupedFaqs.forEach { (category, faqs) ->
                item {
                    val icon = when (category) {
                        "Cuenta y acceso" -> Icons.Default.ManageAccounts
                        "Citas y servicios" -> Icons.Default.EventSeat
                        "Pagos" -> Icons.Default.CreditCard
                        "Atención al cliente" -> Icons.Default.ContactSupport
                        else -> Icons.Default.HelpOutline
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                items(faqs) { faq ->
                    ExpandableFaqCard(faq = faq)
                }
            }
        }
    }
}

@Composable
fun ExpandableFaqCard(faq: FaqItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize() // Anima el cambio de tamaño
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = faq.question,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Contraer" else "Expandir",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = faq.answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}