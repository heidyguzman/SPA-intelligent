package com.narmocorp.satorispa.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.narmocorp.satorispa.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow

class Notifications {

    @Composable
    fun Notificaciones(modifier: Modifier = Modifier) {
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
                        y = 57.dp)
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
                        .offset(x = 11.dp,
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
                    text = "Notificaciones",
                    color = Color(0xff1c1b1f),
                    lineHeight = 5.83.em,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 83.dp,
                            y = 12.dp))
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 20.dp,
                        y = 173.dp)
                    .requiredWidth(width = 372.dp)
                    .requiredHeight(height = 703.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 22.dp,
                            y = 0.dp)
                        .requiredWidth(width = 327.dp)
                        .requiredHeight(height = 703.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = Color(0xffdbbba6)))
                Column(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp,
                            y = 71.dp)
                        .fillMaxWidth()
                        .requiredHeight(height = 77.dp)
                        .clip(shape = RoundedCornerShape(25.dp))
                ) {
                    TableRowChatDarkModeNo()
                }
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp,
                            y = 85.dp)
                        .requiredWidth(width = 63.dp)
                        .requiredHeight(height = 50.dp))
                TableRowChatDarkModeNo(
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .offset(x = 36.dp,
                            y = (-156.5).dp))
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp,
                            y = 171.dp)
                        .requiredWidth(width = 63.dp)
                        .requiredHeight(height = 50.dp))
                TableRowChatDarkModeNo(
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .offset(x = 33.dp,
                            y = (-52).dp))
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp,
                            y = 275.dp)
                        .requiredWidth(width = 63.dp)
                        .requiredHeight(height = 50.dp))
                TableRowChatDarkModeNo(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 67.dp,
                            y = 360.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp,
                            y = 371.dp)
                        .requiredWidth(width = 63.dp)
                        .requiredHeight(height = 50.dp))
            }
        }
    }

    @Composable
    fun TableRowChatDarkModeNo(modifier: Modifier = Modifier, title18968: String, description1960: String) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            modifier = modifier
                .requiredWidth(width = 304.dp)
                .requiredHeight(height = 77.dp)
                .clip(shape = RoundedCornerShape(25.dp))
                .background(color = Color(0xffd9d9d9))
                .padding(horizontal = 16.dp,
                    vertical = 20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .requiredHeight(height = 56.dp)
                    .weight(weight = 1f)
            ) {
                Text(
                    text = title18968,
                    color = AppColors.color_Light_Theme_On_Surface_20,
                    lineHeight = 1.5.em,
                    style = TextStyle(
                        fontSize = 16.sp,
                        letterSpacing = 0.15.sp),
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically))
                Text(
                    text = description1960,
                    color = AppColors.color_Light_Theme_On_Surface_60,
                    lineHeight = 1.43.em,
                    style = TextStyle(
                        fontSize = 14.sp,
                        letterSpacing = 0.25.sp),
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically))
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "3:58 PM",
                    color = AppColors.color_Light_Theme_On_Surface_60,
                    textAlign = TextAlign.End,
                    lineHeight = 1.33.em,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically))
            }
        }
    }

    @Preview(widthDp = 412, heightDp = 917)
    @Composable
    private fun NotificacionesPreview() {
        Notificaciones(Modifier)
    }
}