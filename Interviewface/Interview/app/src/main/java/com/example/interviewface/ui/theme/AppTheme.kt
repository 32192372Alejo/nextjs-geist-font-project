package com.example.interviewface.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.sp
import com.example.interviewface.R

// Definición de colores
val DarkBackground = Color(0xFF1A2530) // Color más oscuro y azulado como en la primera imagen
val BlueAccent = Color(0xFF2196F3)
val YellowAccent = Color(0xFFFFD700)
val TextSecondary = Color(0xFFAAAAAA)
val StandardGray = Color(0xFF2C2C2C) // Color gris estándar para botones

// Definición de tipografía
val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = Color.White
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color.White
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        color = Color.White
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        color = Color.White
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        color = Color.White
    )
)

@Composable
fun InterviewfaceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = androidx.compose.material3.darkColorScheme(
            primary = BlueAccent,
            secondary = YellowAccent,
            background = DarkBackground,
            surface = DarkBackground,
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        ),
        typography = AppTypography,
        content = content
    )
}
