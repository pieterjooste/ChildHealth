package com.childhealth.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

fun getColorFromName(colorName: String): Color {
    return when (colorName.lowercase()) {
        "clear" -> Color.Transparent
        "black" -> Color.Black
        "white" -> Color.White
        "gray" -> Color.Gray
        "red" -> Color.Red
        "green" -> Color(0xFF4CD964)
        "blue" -> Color(0xFF007AFF)
        "orange" -> Color(0xFFFFA500)
        "yellow" -> Color.Yellow
        "pink" -> Color(0xFFFFC0CB)
        "purple" -> Color.Magenta
        "primary" -> Color(0xFF6200EE)
        "secondary" -> Color(0xFF03DAC5)
        else -> Color.White
    }
}