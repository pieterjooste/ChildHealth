package com.childhealth.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Original Colors that worked well
val OriginalBlue = Color(0xFF007AFF)
val OriginalRed = Color.Red

// M3 Success Colors (Green) - Theme Aware for Readability
val SuccessLight = Color(0xFF2E7D32)
val OnSuccessLight = Color(0xFFFFFFFF)
val SuccessDark = Color(0xFF1B5E20) // Darker Green for better contrast in Dark Mode
val OnSuccessDark = Color(0xFFFFFFFF)

// M3 Warning Colors (Yellow/Orange) - Theme Aware for Readability
val WarningLight = Color(0xFFF57C00)
val OnWarningLight = Color(0xFFFFFFFF)
val WarningDark = Color(0xFFE65100) // Darker Orange/Yellow for better contrast in Dark Mode
val OnWarningDark = Color(0xFFFFFFFF)

@Composable
fun getColorFromName(colorName: String): Color {
    val colorScheme = MaterialTheme.colorScheme
    val isLight = colorScheme.primary == Purple40
    
    return when (colorName.lowercase()) {
        "clear" -> Color.Transparent
        "black" -> Color.Black
        "white" -> Color.White
        "gray" -> colorScheme.surfaceVariant
        "red" -> OriginalRed
        "green" -> if (isLight) SuccessLight else SuccessDark
        "blue" -> OriginalBlue
        "orange", "yellow" -> if (isLight) WarningLight else WarningDark
        "pink" -> Color(0xFFFFC0CB)
        "purple" -> Color.Magenta
        "primary" -> colorScheme.primary
        "secondary" -> colorScheme.secondary
        else -> colorScheme.surface
    }
}

@Composable
fun getOnColorFromName(colorName: String): Color {
    val colorScheme = MaterialTheme.colorScheme
    val isLight = colorScheme.primary == Purple40

    return when (colorName.lowercase()) {
        "red" -> Color.White
        "green" -> if (isLight) OnSuccessLight else OnSuccessDark
        "orange", "yellow" -> if (isLight) OnWarningLight else OnWarningDark
        "gray" -> colorScheme.onSurfaceVariant
        "blue" -> Color.White
        "primary" -> colorScheme.onPrimary
        "secondary" -> colorScheme.onSecondary
        else -> colorScheme.onSurface
    }
}
