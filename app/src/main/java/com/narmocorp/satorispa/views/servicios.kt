package com.narmocorp.satorispa.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.narmocorp.satorispa.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

// Data class for service
data class ServicioItem(val title: String, val price: String, val imageRes: Int)

val serviciosList = listOf(
    ServicioItem("Masaje relajante", "$150.00", R.drawable.placeholder),
    ServicioItem("Masaje con piedras calientes", "$180.00", R.drawable.placeholder),
    ServicioItem("Masaje aromaterapéutico", "$200.00", R.drawable.placeholder),
    ServicioItem("Masaje de tejido profundo", "$240.00", R.drawable.placeholder)
)

@Composable
fun Servicios(
    modifier: Modifier = Modifier,
    onHomeClick: (() -> Unit)? = null,
    onServiciosClick: (() -> Unit)? = null,
    onCitasClick: (() -> Unit)? = null,
    onNavigateToNotifications: (() -> Unit)? = null,
    onNavigateToConfig: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopBar(
                onNavigateToNotifications = onNavigateToNotifications,
                onNavigateToConfig = onNavigateToConfig
            )
        },
        bottomBar = {
            NavBar(
                onHomeClick = onHomeClick,
                onServiciosClick = onServiciosClick,
                onCitasClick = onCitasClick
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Servicios",
                color = Color(0xff1c1b1f),
                lineHeight = 4.24.em,
                style = TextStyle(fontSize = 33.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Search Bar
            Box(
                modifier = Modifier
                    .width(279.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xffd9d9d9).copy(alpha = 0.8f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "search",
                        tint = Color(0xff1c1b1f),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Buscar",
                        color = Color(0xffc9c9ca),
                        style = TextStyle(fontSize = 20.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Servicios Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(serviciosList) { servicio ->
                    ServicioCard(servicio)
                }
            }
        }
    }
}

@Composable
fun ServicioCard(servicio: ServicioItem) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painter = painterResource(id = servicio.imageRes),
                    contentDescription = servicio.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = servicio.title,
                    style = TextStyle(fontSize = 16.sp, color = Color(0xff374151)),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = servicio.price,
                    style = TextStyle(fontSize = 14.sp, color = Color(0xff757575)),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Icon(
                imageVector = Icons.Default.EventAvailable,
                contentDescription = "Agregar a calendario",
                tint = Color(0xff374151),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(38.dp)
                    .padding(8.dp)
            )
        }
    }
}

@Preview(widthDp = 412, heightDp = 917)
@Composable
private fun ServiciosPreview() {
    Servicios()
}
