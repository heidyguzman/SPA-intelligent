package com.narmocorp.satorispa.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.narmocorp.satorispa.R

private val color_M3_sys_light_on_surface = Color(0xff1c1b1f)

@Composable
fun Configuracion(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 412.dp)
            .requiredHeight(height = 917.dp)
            .background(color = Color.White)
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 37.dp,
                    y = 27.dp)
                .requiredWidth(width = 341.dp)
                .requiredHeight(height = 62.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 341.dp)
                    .requiredHeight(height = 62.dp)
                    .clip(shape = RoundedCornerShape(50.dp))
                    .background(color = Color(0xffdbbba6)))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 9.dp,
                        y = 8.dp)
                    .requiredWidth(width = 48.dp)
                    .requiredHeight(height = 45.dp)
            ) {
                Box(
                    modifier = Modifier
                        .requiredWidth(width = 48.dp)
                        .requiredHeight(height = 45.dp)
                        .background(color = Color(0xff995d2d)))
                Image(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = "arrow_back",
                    colorFilter = ColorFilter.tint(Color(0xff1c1b1f)),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 9.41.dp,
                            y = 7.58.dp)
                        .requiredSize(size = 29.dp))
            }
            Text(
                text = "Configuración",
                color = Color(0xff1c1b1f),
                lineHeight = 5.83.em,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 78.dp,
                        y = 16.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 44.dp,
                    y = 89.dp)
                .requiredWidth(width = 327.dp)
                .requiredHeight(height = 724.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 327.dp)
                    .requiredHeight(height = 703.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = Color(0xffdbbba6)))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 85.dp,
                        y = 34.dp)
                    .requiredWidth(width = 158.dp)
                    .requiredHeight(height = 160.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.account_circle),
                    contentDescription = "account_circle",
                    colorFilter = ColorFilter.tint(color_M3_sys_light_on_surface),
                    modifier = Modifier
                        .requiredWidth(width = 158.dp)
                        .requiredHeight(height = 160.dp))
            }
            Text(
                text = "Nombre",
                color = Color(0xff1c1b1f),
                lineHeight = 4.24.em,
                style = TextStyle(
                    fontSize = 33.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 98.dp,
                        y = 227.dp))
            Text(
                text = "Mi cuenta",
                color = Color(0xff1c1b1f),
                lineHeight = 5.83.em,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 26.dp,
                        y = 318.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 16.dp,
                        y = 352.dp)
                    .requiredWidth(width = 292.dp)
                    .requiredHeight(height = 72.dp)
            ) {
                Box(
                    modifier = Modifier
                        .requiredWidth(width = 292.dp)
                        .requiredHeight(height = 72.dp)
                        .clip(shape = RoundedCornerShape(50.dp))
                        .background(color = Color(0xff995d2d).copy(alpha = 0.79f)))
                Text(
                    text = "Editar Perfil",
                    color = Color(0xff1c1b1f),
                    lineHeight = 5.83.em,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 82.dp,
                            y = 15.dp))
                Image(
                    painter = painterResource(id = R.drawable.edit_square),
                    contentDescription = "edit_square",
                    colorFilter = ColorFilter.tint(Color(0xff1c1b1f)),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 221.dp,
                            y = 17.dp)
                        .requiredSize(size = 24.dp))
            }
            Text(
                text = "Seguridad",
                color = Color(0xff1c1b1f),
                lineHeight = 5.83.em,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 16.dp,
                        y = 457.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 16.dp,
                        y = 499.dp)
                    .requiredWidth(width = 292.dp)
                    .requiredHeight(height = 72.dp)
            ) {
                Box(
                    modifier = Modifier
                        .requiredWidth(width = 292.dp)
                        .requiredHeight(height = 72.dp)
                        .clip(shape = RoundedCornerShape(50.dp))
                        .background(color = Color(0xff995d2d).copy(alpha = 0.79f)))
                Text(
                    text = "Borrar Cuenta",
                    color = Color.Black,
                    lineHeight = 5.83.em,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 64.dp,
                            y = 15.dp))
                Image(
                    painter = painterResource(id = R.drawable.no_accounts),
                    contentDescription = "no_accounts",
                    colorFilter = ColorFilter.tint(Color(0xff1c1b1f)),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 227.dp,
                            y = 22.dp)
                        .requiredSize(size = 24.dp))
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 85.dp,
                        y = 682.dp)
                    .requiredWidth(width = 176.dp)
                    .requiredHeight(height = 42.dp)
            ) {
                Text(
                    text = "Cerrar Sesion",
                    color = Color.Red,
                    textDecoration = TextDecoration.Underline,
                    lineHeight = 5.83.em,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Image(
                    painter = painterResource(id = R.drawable.chip_extraction),
                    contentDescription = "chip_extraction",
                    colorFilter = ColorFilter.tint(Color(0xff1c1b1f)),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 152.dp,
                            y = 9.dp)
                        .requiredSize(size = 24.dp))
            }
        }
    }
}

@Preview(widthDp = 412, heightDp = 917)
@Composable
private fun ConfiguracionPreview() {
    Configuracion(Modifier)
}