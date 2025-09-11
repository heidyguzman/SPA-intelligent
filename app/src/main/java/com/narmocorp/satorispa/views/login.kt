package com.narmocorp.satorispa.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun Login(modifier: Modifier = Modifier, label1901: String) {
    Box(
        modifier = modifier
            .requiredWidth(width = 412.dp)
            .requiredHeight(height = 917.dp)
            .background(color = Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.image2),
            contentDescription = "image 2",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .requiredWidth(width = 412.dp)
                .requiredHeight(height = 404.dp))
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 85.dp,
                    y = 24.dp)
                .requiredWidth(width = 239.dp)
                .requiredHeight(height = 190.dp))
        Image(
            painter = painterResource(id = R.drawable.rectangle2),
            contentDescription = "Rectangle 2",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 1.dp,
                    y = 265.dp)
                .requiredWidth(width = 410.dp)
                .requiredHeight(height = 652.dp)
                .clip(shape = RoundedCornerShape(30.dp)))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 37.dp,
                    y = 289.dp)
                .requiredWidth(width = 340.dp)
                .requiredHeight(height = 101.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 340.dp)
                    .requiredHeight(height = 101.dp)
                    .clip(shape = RoundedCornerShape(100.dp))
                    .background(color = Color(0xffdbbba6).copy(alpha = 0.86f)))
            Image(
                painter = painterResource(id = R.drawable.rectangle4),
                contentDescription = "Rectangle 4",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 12.dp,
                        y = 13.dp)
                    .requiredWidth(width = 174.dp)
                    .requiredHeight(height = 74.dp)
                    .clip(shape = RoundedCornerShape(100.dp)))
            Text(
                text = "Inicio",
                color = Color.White,
                lineHeight = 4.24.em,
                style = TextStyle(
                    fontSize = 33.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 58.dp,
                        y = 26.dp)
                    .requiredWidth(width = 82.dp)
                    .requiredHeight(height = 47.dp))
            Text(
                text = "Registro",
                color = Color.White,
                lineHeight = 4.24.em,
                style = TextStyle(
                    fontSize = 33.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 197.dp,
                        y = 26.dp)
                    .requiredWidth(width = 129.dp)
                    .requiredHeight(height = 47.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 25.dp,
                    y = 404.dp)
                .requiredWidth(width = 360.dp)
                .requiredHeight(height = 433.dp)
        ) {
            Text(
                text = "SATORI SPA TE DA LA BIENVENIDA",
                color = Color(0xff71390c).copy(alpha = 0.79f),
                textAlign = TextAlign.Center,
                lineHeight = 6.09.em,
                style = TextStyle(
                    fontSize = 23.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 39.dp,
                        y = 0.dp)
                    .border(border = BorderStroke(1.dp, Color(0xff995d2d).copy(alpha = 0.5f))))
            TextField(
                value = "",
                onValueChange = {},
                label = {
                    Text(
                        text = label1901,
                        color = AppColors.color_Light_Theme_On_Surface_60,
                        lineHeight = 1.5.em,
                        style = AppTypes.type_Subtitle_1,
                        modifier = Modifier
                            .wrapContentHeight(align = Alignment.CenterVertically))
                },
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 39.dp,
                        y = 98.dp)
                    .requiredWidth(width = 321.dp)
                    .padding(horizontal = 16.dp))
            Icon(
                painter = painterResource(id = R.drawable.mail),
                contentDescription = "mail",
                tint = AppColors.color_M3_sys_light_on_surface,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 15.dp,
                        y = 114.dp))
            TextField(
                value = "",
                onValueChange = {},
                label = {
                    Text(
                        text = label1901,
                        color = AppColors.color_Light_Theme_On_Surface_60,
                        lineHeight = 1.5.em,
                        style = AppTypes.type_Subtitle_1,
                        modifier = Modifier
                            .wrapContentHeight(align = Alignment.CenterVertically))
                },
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 39.dp,
                        y = 178.dp)
                    .requiredWidth(width = 321.dp)
                    .padding(horizontal = 16.dp))
            Image(
                painter = painterResource(id = R.drawable.formlogin),
                contentDescription = "Form Log In",
                colorFilter = ColorFilter.tint(Color(0xff1c1b1f)),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 22.dp,
                        y = 204.dp)
                    .requiredWidth(width = 23.dp))
            Image(
                painter = painterResource(id = R.drawable.eyeoff),
                contentDescription = "Eye off",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 303.dp,
                        y = 204.dp)
                    .requiredWidth(width = 30.dp)
                    .requiredHeight(height = 21.dp))
            TextField(
                value = "",
                onValueChange = {},
                label = {
                    Text(
                        text = label1901,
                        color = AppColors.color_Light_Theme_On_Surface_60,
                        lineHeight = 1.5.em,
                        style = AppTypes.type_Subtitle_1,
                        modifier = Modifier
                            .wrapContentHeight(align = Alignment.CenterVertically))
                },
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 0.dp,
                        y = 258.dp)
                    .requiredWidth(width = 360.dp)
                    .padding(start = 56.dp,
                        end = 16.dp))
            Image(
                painter = painterResource(id = R.drawable.eyeoff),
                contentDescription = "Eye off",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 302.dp,
                        y = 287.dp)
                    .requiredWidth(width = 30.dp)
                    .requiredHeight(height = 21.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 104.dp,
                        y = 339.dp)
                    .requiredWidth(width = 178.dp)
                    .requiredHeight(height = 46.dp)
            ) {
                Box(
                    modifier = Modifier
                        .requiredWidth(width = 178.dp)
                        .requiredHeight(height = 46.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(color = Color(0xffdbbba6))
                        .border(border = BorderStroke(1.dp, Color(0xff995d2d).copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(30.dp)))
                Text(
                    text = "Iniciar",
                    color = Color(0xff995d2d),
                    lineHeight = 1.27.em,
                    style = AppTypes.type_M3_title_large,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 49.11.dp,
                            y = 4.32.dp)
                        .requiredWidth(width = 91.dp)
                        .requiredHeight(height = 37.dp)
                        .border(border = BorderStroke(1.dp, Color(0xff995d2d).copy(alpha = 0.5f)))
                        .wrapContentHeight(align = Alignment.CenterVertically))
                Image(
                    painter = painterResource(id = R.drawable.input),
                    contentDescription = "input",
                    colorFilter = ColorFilter.tint(Color(0xff1c1b1f)),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 133.dp,
                            y = 11.dp)
                        .requiredSize(size = 24.dp))
            }
            Text(
                text = "Olvide contraseña?",
                color = ColorsColor.color_Text_Default_Default.invoke(),
                textDecoration = TextDecoration.Underline,
                lineHeight = 8.75.em,
                style = AppTypes.type_Body_Link,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 123.dp,
                        y = 411.dp))
        }
    }
}

@Preview(widthDp = 412, heightDp = 917)
@Composable
private fun LoginPreview() {
    Login(Modifier, "Correo")
}

